//
//  ViewController.m
//  SocialLibrarySample
//
//  Created by IE Infoedge on 27/04/17.
//  Copyright © 2017 IE Infoedge. All rights reserved.
//

#import "SampleViewController.h"
//#import <IESocial/IESocial.h>
#import "SocialHelper.h"
#define GOOGLE_SIGN_IN_CLIENT_ID @"14524039113-mfd99ejudup7tcaj8e1u9sh7kprso8i9.apps.googleusercontent.com"  //Generated from acount: marketing.naukrigulf@gmail.com

@interface SampleViewController ()

@end

@implementation SampleViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Actions

- (IBAction)fbLoginAction:(id)sender {
    SocialModel *model = [SocialModel new];
    model.baseVc = self;
    model.loginType =  k_SocialLoginFB;
    // Either Add the Permissions Required or set it to default

    // model.fbPermissionDict= {@"public_profile":@"name,gender,first_name,last_name"}
    [self setUpSocialHelperWithModel:model];

}

- (IBAction)fbLogoutAction:(id)sender {
    [SocialHelper socialLogout];
}

- (IBAction)gmailSignInLoginAction:(id)sender {
    SocialModel *model = [SocialModel new];
    model.baseVc = self;
    /// Add Client Id
    model.clientID = @"14524039113-mfd99ejudup7tcaj8e1u9sh7kprso8i9.apps.googleusercontent.com";
    model.loginType =  k_SocialLoginGmail;
    // Either Add the Permissions Required or set it to default
   // model.gmailPermissionArray = [@"https://www.googleapis.com/auth/plus.stream.read"];
    [self setUpSocialHelperWithModel:model];

}

- (IBAction)gmailSignInLogoutAction:(id)sender {
    [SocialHelper socialLogout];
}

#pragma mark - Private Method
- (void)setUpSocialHelperWithModel:(SocialModel *)model {
    
    [SocialHelper socialLoginWithModel:model successHandler:^(ParsedDataModel *dataModel) {
        NSLog(@"Data Model Social Type %@",dataModel.socialKey);
        NSLog(@"Data Model Social Type %@",dataModel.name);
        NSLog(@"Data Model Social Type %@",dataModel.email);
        NSLog(@"Data Model Social Type %@",dataModel.userId);
        NSLog(@"Data Model Social Type %@",dataModel.userTokenKey);
        NSLog(@"Data Model Social Type %@",dataModel.tokenExpiredDate);
        NSLog(@"Data Model Social Type %@",dataModel.unParsedFieldDict);
    } errorHanlder:^(NSError *error, BOOL isCanceled) {
        NSLog(@"User Canceled Login Process %@",isCanceled ? @"true" :@"false");
        NSLog(@"Error Found is %@",error.localizedDescription);
    }];
}


@end