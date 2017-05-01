//
//  SocialGmailService.m
//  ExampleSocialSample
//
//  Copyright © 2017 INFOEDGE INDIA LTD. All rights reserved.
//

#import "SocialGmailService.h"
#import <GoogleSignIn/GoogleSignIn.h>

@interface SocialGmailService()<GIDSignInDelegate, GIDSignInUIDelegate> {
    BOOL fetchBasicProfile;
}

@property (nonatomic , copy) void (^successHandler)(ParsedDataModel *dataModel);
@property (nonatomic , copy) void (^failureHandler)(NSError* error , BOOL isCanceled);
@property (nonatomic , copy) void (^showGmailVc)(BOOL finished, UIViewController *vc);

@end

@implementation SocialGmailService
+ (SocialGmailService *)gmailInstance {
    static SocialGmailService *sharedInstance =  nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance  =  [[SocialGmailService alloc]init];
    });
    return sharedInstance;
}

#pragma mark - Public
- (BOOL)openURL:(NSURL *)url withSourceApplication:(NSString *)sourceApplication annotation:(id)annotation {
        return [[GIDSignIn sharedInstance] handleURL:url sourceApplication:sourceApplication annotation:annotation];
}

- (void)initializeGmail:(SocialModel *)model withSuccessHandler:(void(^)(ParsedDataModel *dataModel))successCompletion errorHanlder:(void(^)(NSError* error , BOOL isCanceled))errorCompletion {
    self.successHandler = successCompletion;
    self.failureHandler = errorCompletion;

    GIDSignIn *signIn = [GIDSignIn sharedInstance];
    signIn.clientID = model.clientID;
    if (model.gmailPermissionArray == nil || model.gmailPermissionArray.count == 0) {
        signIn.scopes =  @[@"https://www.googleapis.com/auth/plus.stream.read"];
        fetchBasicProfile = true;
    }else {
        signIn.scopes =  model.gmailPermissionArray;
        fetchBasicProfile = false;
    }
    signIn.shouldFetchBasicProfile =  true;
    [self showGmailSignInScreen:^(BOOL showVC, UIViewController *vc) {
        if (showVC == true) {
            [model.baseVc presentViewController:vc animated:true completion:nil];
        }
        else {
            [model.baseVc dismissViewControllerAnimated:true completion:nil];
        }
    }];
}

- (void)gmailLogout {
    if ([GIDSignIn sharedInstance].hasAuthInKeychain) {
        [[GIDSignIn sharedInstance]signOut];
    }
}

#pragma mark : - Private

/// This Method is used for Calling Socil View Controller and Setting Up ui and functional delegate
- (void)showGmailSignInScreen:(void (^)(BOOL showVC , UIViewController *vc ))blocker {
    self.showGmailVc =  blocker;
    [GIDSignIn sharedInstance].delegate = self;
    [GIDSignIn sharedInstance].uiDelegate = self;
    [[GIDSignIn sharedInstance] signIn];
}

// Fetch Gmail Parse Modal
-(void)fetchGmailProfileWithAuthToken:(NSString *)accessToken {
    NSURLSessionConfiguration *confi = [NSURLSessionConfiguration defaultSessionConfiguration];
    NSURLSession* session = [NSURLSession sessionWithConfiguration:confi delegate:nil delegateQueue:nil];
    NSString *requestString =[NSString stringWithFormat:@"https://www.googleapis.com/plus/v1/people/me?access_token=\%@",accessToken];

    NSURL *url = [NSURL URLWithString:requestString];
    NSURLRequest *req = [NSURLRequest requestWithURL:url];
    __weak typeof(self) weakSelf = self;

    NSURLSessionDataTask *dataTask = [session dataTaskWithRequest:req completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        NSDictionary *jsonObject = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
        if (error || [jsonObject objectForKey:@"error"]){
            [weakSelf showGmailErrorHandler:error];
            return;
        }
        [weakSelf createParseModelWithDict:jsonObject];
    }];
    [dataTask resume];
}

#pragma mark - Private 
- (void)showGmailErrorHandler:(NSError *)error {
    BOOL isCancel = false;
    if ([error code] == - 5) {
        isCancel = true;
    }
    self.failureHandler(error, isCancel);
}

- (void)createParseModelWithDict:(NSDictionary *)dict {
    ParsedDataModel *dataModel = [ParsedDataModel new];
    GIDGoogleUser *currentUser = [[GIDSignIn sharedInstance] currentUser];
    dataModel.userId = [currentUser userID];
    dataModel.userTokenKey = [[currentUser authentication] idToken];
    dataModel.tokenExpiredDate = [[currentUser authentication] accessTokenExpirationDate];
    dataModel.socialKey = @"gp";

    dataModel.email =  currentUser.profile.email;
    dataModel.name =  currentUser.profile.name;

    if (dict != nil && dict.allKeys.count > 0) {
         dataModel.unParsedFieldDict =  dict;
    }
    self.successHandler(dataModel);
}

#pragma mark : - GIDSignInUIDelegate
- (void)signInWillDispatch:(GIDSignIn *)signIn error:(NSError *)error {
    if (error) {
        [self showGmailErrorHandler:error];
    }
}

// Present a view that prompts the user to sign in with Google
- (void)signIn:(GIDSignIn *)signIn presentViewController:(UIViewController *)viewController {
    if (viewController == nil || signIn == nil || self.showGmailVc == nil) {
        return;
    }
    self.showGmailVc(true , viewController);
}

// Dismiss the "Sign in with Google" view
- (void)signIn:(GIDSignIn *)signIn dismissViewController:(UIViewController *)viewController {
    if (viewController == nil || signIn == nil || self.showGmailVc == nil) {
        return;
    }
    self.showGmailVc(false , viewController);
}

#pragma mark :- GIDSignIn Delegate
- (void)signIn:(GIDSignIn *)signIn didSignInForUser:(GIDGoogleUser *)user withError:(NSError *)error {
    if(error) {
        [self showGmailErrorHandler:error];
    }
    
    if (fetchBasicProfile) {
        [self createParseModelWithDict:nil];
    }else {
        [self fetchGmailProfileWithAuthToken:signIn.currentUser.authentication.accessToken];
    }
}

- (void)signIn:(GIDSignIn *)signIn didDisconnectWithUser:(GIDGoogleUser *)user
     withError:(NSError *)error {
    if (error) { [self showGmailErrorHandler:error];    }
}

@end
