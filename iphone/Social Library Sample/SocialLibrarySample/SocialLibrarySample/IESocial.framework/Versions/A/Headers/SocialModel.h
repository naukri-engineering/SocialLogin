//
//  SocialModel.h
//  SocialLibrary
//
//  Created by RahulSharma on 3/21/17.
//  Copyright © 2017 INFOEDGE INDIA LTD. All rights reserved.
//


#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "SocialConstant.h"

@interface SocialModel : NSObject
///Controller for Showing facebook  / Gmail dialog
@property (nonatomic , strong) UIViewController *baseVc;

/// FB / Gmail appId or key
@property (nonatomic , strong) NSString *clientID;

/// Gmail / FB Login Enum
@property (nonatomic) SocialLoginType loginType;

/*! @brief Permissions required in Facebook 
 // Example {@"public_profile":@"name,gender,first_name,last_name"}
 // Key = @"public_profile"
 // Value = @"name,gender,first_name,last_name"
 // If No Permission is given a default permission is sent for Getting basic user info

 */
@property (nonatomic , strong) NSDictionary *fbPermissionDict;
/*! Permission  Required in Gmail Signin
    // Example [@"https://www.googleapis.com/auth/plus.stream.read"]
    // If No Permission is given a default permission is sent for Getting basic user info
 */
@property (nonatomic , strong) NSArray *gmailPermissionArray;

@end
