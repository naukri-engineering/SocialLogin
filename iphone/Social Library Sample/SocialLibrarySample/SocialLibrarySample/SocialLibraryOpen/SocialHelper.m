//
//  SocialSharedManager.m
//  SocialLibrary
//
//  Copyright © 2017 INFOEDGE INDIA LTD. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SocialHelper.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>

#import "SocialFbService.h"
#import "SocialGmailService.h"

@interface SocialHelper()
@end

@implementation SocialHelper
#pragma mark:- Application Delegate
+ (void)didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    [[SocialFbService fbInstance]openApplicationWithLaunchOption:launchOptions];
}

+ (BOOL)openURL:(NSURL *)url sourceApplication: (NSString *)sourceApplication annotation:(id)annotation {
    if ([url.absoluteString rangeOfString:@"fb" options:NSCaseInsensitiveSearch range:NSMakeRange(0, 2)].location != NSNotFound){
       return [[SocialFbService fbInstance]openURL:url withSourceApplication:sourceApplication annotation:annotation];
    }
    else if ([url.absoluteString rangeOfString:@"com.googleusercontent.apps" options:NSCaseInsensitiveSearch].location != NSNotFound) {
       return [[SocialGmailService gmailInstance]openURL:url withSourceApplication:sourceApplication annotation:annotation];
    }
    return false;
}

#pragma mark: - Public
+ (void)socialLoginWithModel:(SocialModel *)socialModel successHandler:(void(^)(ParsedDataModel *dataModel))successCompletion errorHanlder:(void(^)(NSError* error , BOOL isCanceled))errorCompletion {
    [SocialHelper checkSocilModel:socialModel];
    if (socialModel.loginType == k_SocialLoginFB) {
        [[SocialFbService fbInstance]initializeFb:socialModel withSuccessHandler:^(ParsedDataModel *dataModel) {
            successCompletion(dataModel);
        } errorHanlder:^(NSError *error, BOOL isCanceled) {
            errorCompletion(error, isCanceled);
        }];
    }
    else if (socialModel.loginType == k_SocialLoginGmail) {
        [[SocialGmailService gmailInstance]initializeGmail:socialModel withSuccessHandler:^(ParsedDataModel *dataModel) {
            successCompletion(dataModel);
        } errorHanlder:^(NSError *error, BOOL isCanceled) {
            errorCompletion(error,isCanceled);
        }];
    }
    else {
    }
}

// FaceBook and GMail Logout
+ (void)socialLogout {
    /// Show Facebook logout
    /// Show Gmail Logout
    NSLog(@"Social Logout Tapped");
    [[SocialFbService fbInstance]facebookLogout];
    [[SocialGmailService gmailInstance]gmailLogout];
}

#pragma mark :- Private
//Check If Required Field Available For proper functioning
+ (void)checkSocilModel:(SocialModel *)model {
    if (model.loginType == k_SocialLoginNone) {
        NSAssert(NO, @"Added Social Login Type");
    }
    
    if (model.baseVc == nil) {
        NSAssert(NO, @"Added View Controller on Which Fb / Gmail Dialog should shown");
    }

    if (model.loginType == k_SocialLoginGmail) {
        if (model.clientID == nil || model.clientID.length == 0) {
            NSAssert(NO, @"Add Client ID For Gmail");
        }
    }
}
@end
