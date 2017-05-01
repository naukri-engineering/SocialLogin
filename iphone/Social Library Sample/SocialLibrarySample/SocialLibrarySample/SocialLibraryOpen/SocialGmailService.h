//
//  SocialGmailService.h
//  ExampleSocialSample
//
//  Copyright © 2017 INFOEDGE INDIA LTD. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ParsedDataModel.h"
#import "SocialModel.h"

@interface SocialGmailService : NSObject
+ (SocialGmailService *)gmailInstance;

- (BOOL)openURL:(NSURL *)url withSourceApplication:(NSString *)sourceApplication annotation:(id)annotation;

- (void)initializeGmail:(SocialModel *)model withSuccessHandler:(void(^)(ParsedDataModel *dataModel))successCompletion errorHanlder:(void(^)(NSError* error , BOOL isCanceled))errorCompletion;

- (void)gmailLogout;

@end
