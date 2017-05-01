//
//  SocialFbService.m
//  ExampleSocialSample
//
//  Copyright © 2017 INFOEDGE INDIA LTD. All rights reserved.
//

#import "SocialFbService.h"
#import <UIKit/UIKit.h>
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>


@interface SocialFbService() {
      FBSDKLoginManager *fbManager;
}

@property (nonatomic , copy) void (^successHandler)(ParsedDataModel *dataModel);
@property (nonatomic , copy) void (^failureHandler)(NSError* error , BOOL isCanceled);

@end

@implementation SocialFbService
#pragma mark : Public
+ (SocialFbService *)fbInstance{
    static SocialFbService *sharedInstance =  nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance  =  [[SocialFbService alloc]init];
    });
    return sharedInstance;
}

- (UIApplication *)application {
    return [UIApplication sharedApplication];
}

- (void)openApplicationWithLaunchOption:(NSDictionary *)launchOptions  {
    [[FBSDKApplicationDelegate sharedInstance] application:[self application] didFinishLaunchingWithOptions:launchOptions];
}

- (BOOL)openURL:(NSURL *)url withSourceApplication: (NSString *)sourceApplication annotation:(id)annotation {
    return [[FBSDKApplicationDelegate sharedInstance] application:[self application] openURL:url sourceApplication:sourceApplication annotation:annotation];
}

- (void)fbActivateApp{
    [FBSDKAppEvents activateApp];
}

- (void)facebookLogout{
    if ([FBSDKAccessToken currentAccessToken] != nil) {
        [FBSDKAccessToken setCurrentAccessToken:nil];
        [fbManager logOut];
         fbManager = nil;
    }
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIApplicationDidBecomeActiveNotification object:nil];
}

- (void)initializeFb:(SocialModel *)model withSuccessHandler:(void(^)(ParsedDataModel *dataModel))successCompletion errorHanlder:(void(^)(NSError* error , BOOL isCanceled))errorCompletion {
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(fbActivateApp) name:UIApplicationDidBecomeActiveNotification object:nil];

    [FBSDKAccessToken setCurrentAccessToken:nil];
    fbManager =  nil;
    fbManager =  [FBSDKLoginManager new];
    fbManager.loginBehavior = FBSDKLoginBehaviorBrowser;
    
    __weak typeof(self) weakSelf = self;
    self.successHandler = successCompletion;
    self.failureHandler =  errorCompletion;
    
    NSMutableDictionary *perm = nil;
    if (model.fbPermissionDict == nil || model.fbPermissionDict.allKeys.count == 0 ) {
        perm = [NSMutableDictionary dictionaryWithDictionary:@{@"public_profile":@"name,gender,first_name,last_name",@"email":@"email"}];
    }else {
        perm = [NSMutableDictionary dictionaryWithDictionary:model.fbPermissionDict];
    }
    
    [fbManager logInWithReadPermissions:perm.allKeys fromViewController:model.baseVc handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
        if (error) { weakSelf.failureHandler(error,false); }
        else if (result.isCancelled) { weakSelf.failureHandler(nil,true); }
        else {
            if (result.declinedPermissions.count > 0) {
                [perm removeObjectsForKeys:result.declinedPermissions.allObjects];
            }
            if (result.grantedPermissions.count > 0) {
                [weakSelf fetchUserProfileWithPerm:perm];
            }
         }
    }];
}

#pragma mark:- Private
- (NSString *)fetchFieldValueFromPermissions:(NSDictionary *)perm {
    NSString *fieldValue = @"id";
    for (NSString *keyValue in perm.allKeys) {
            /// Granted Permission as keyValue
            fieldValue = [fieldValue stringByAppendingString:[NSString stringWithFormat:@",%@",[perm objectForKey:keyValue]]];
        }
    return fieldValue;
}

- (void)fetchUserProfileWithPerm:(NSDictionary *)perm{
    __weak typeof(self) weakSelf = self;
    FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me" parameters:@{@"fields": [self fetchFieldValueFromPermissions:perm]} HTTPMethod:@"GET"];
    [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
        if (error != nil) {
            weakSelf.failureHandler(error, false);
        }
        else {
            // successHandler
            [weakSelf createParseModelWithDict:result];
        }
    }];
}

- (void)createParseModelWithDict:(NSDictionary *)dict {
    ParsedDataModel *dataModel = [ParsedDataModel new];
    FBSDKAccessToken *access = [FBSDKAccessToken currentAccessToken];
    dataModel.socialKey = @"fb";
    dataModel.userId = [access userID];
    dataModel.userTokenKey = [access tokenString];
    dataModel.tokenExpiredDate = [access expirationDate];
    
    NSArray *allKeys = [dict allKeys];
    if ([allKeys containsObject:@"name"]) { dataModel.name = [dict objectForKey:@"name"]; }
    if ([allKeys containsObject:@"email"]) { dataModel.email = [dict objectForKey:@"email"];
        dataModel.unParsedFieldDict =  dict;
    }
    self.successHandler(dataModel);
}

@end
