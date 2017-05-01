//
//  SocialSharedManager.h
//  SocialLibrary
//
//  Copyright © 2017 INFOEDGE INDIA LTD. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SocialModel.h"
#import "ParsedDataModel.h"

@interface SocialHelper : NSObject

//Mark:- Implement App Delegate
+ (void)didFinishLaunchingWithOptions:(NSDictionary *)launchOptions;
+ (BOOL)openURL:(NSURL *)url sourceApplication: (NSString *)sourceApplication annotation:(id)annotation;

//MARK:- Login  , Logout , Updation

/*!
 @brief Method is used for Login To facebook and gmail and uses two block success handler for getting parsed response data and error handler for getting error and isCancel.
 @param socialModel The input value is used for adding necessary attribute required during login
 @param successCompletion The input value gives User Profile Data model
 @param errorCompletion The Input Value give two arguments @"error" if present and @"isCanceled"
  if signin user cancel signin process
*/
+ (void)socialLoginWithModel:(SocialModel *)socialModel successHandler:(void(^)(ParsedDataModel *dataModel))successCompletion errorHanlder:(void(^)(NSError* error , BOOL isCanceled))errorCompletion;

/*! Method For Logging Out User */
+ (void)socialLogout;
@end
