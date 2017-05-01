//
//  SocialFbService.h
//  ExampleSocialSample
//
//  Copyright © 2017 INFOEDGE INDIA LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "SocialModel.h"
#import "SocialConstant.h"
#import "ParsedDataModel.h"

@interface SocialFbService : NSObject

+ (SocialFbService *)fbInstance;

- (void)openApplicationWithLaunchOption:(NSDictionary *)launchOptions;
- (BOOL)openURL:(NSURL *)url withSourceApplication: (NSString *)sourceApplication annotation:(id)annotation;
- (void)fbActivateApp;
- (void)facebookLogout;
- (void)initializeFb:(SocialModel *)model withSuccessHandler:(void(^)(ParsedDataModel *dataModel))successCompletion errorHanlder:(void(^)(NSError* error , BOOL isCanceled))errorCompletion ;

@end
