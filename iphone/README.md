# Social Library
***
This is a wrapper library that helps to integrate social login for Gmail and Facebook quickly.

## Feature
1. Allows to customize permissions for Facebook and GmailSignin
2. Compatible with iOS 8.1 and above
3. Automatic exception handling
4. Allows to access social profile of users in just a few lines.

## Integration
To integrate the library in your application, follow the below mentioned steps:
1. Download the framework in your project.
2. For Objective C project, add `#import <IESocial/IESocial.h>`in file of project.
3. For Swift project, write `#import <IESocial/IESocial.h>` in Bridging header file of project.
```
// Add FaceBook
Download the SDK at https://developers.facebook.com/docs/ios or via CocoaPods by adding the 'FBSDKCoreKit', 'FBSDKLoginKit', and  'FBSDKShareKit' pods.
```
``` Gmail
// Add Gmail
Download the SDK at https://developers.google.com/identity/sign-in/ios/sdk/ or via CocoaPods by adding the pod 'Google/SignIn'.
```
### Usage
Configuring  and using Social Library is easy:
```
// Social Model Class is to be created
// For Facebook Initialisation
SocialModel *model = [SocialModel new];
model.baseVc = self;
model.loginType =  k_SocialLoginFB;
[self setUpSocialHelperWithModel:model];
```

```
// Social Model Class is to be created
// For GmailSignIn Initialisation
SocialModel *model = [SocialModel new];
model.baseVc = self;
model.clientID = @“”; // Add ClientId
model.loginType =  k_SocialLoginGmail;
[self setUpSocialHelperWithModel:model];
```

```
// Add Method
- (void)setUpSocialHelperWithModel:(SocialModel *)model {
[SocialHelper socialLoginWithModel:model successHandler:^(ParsedDataModel *dataModel) {
NSLog(@"Data Model Social Key %@",dataModel.socialKey);
NSLog(@"Data Model Social Name %@",dataModel.name);
NSLog(@"Data Model Social Email %@",dataModel.email);
NSLog(@"Data Model Social UserId %@",dataModel.userId);
NSLog(@"Data Model Social userToken Key %@",dataModel.userTokenKey);
NSLog(@"Data Model Social tokenExpiredDate %@",dataModel.tokenExpiredDate);
NSLog(@"Data Model Social unParsedFieldDict %@",dataModel.unParsedFieldDict);
} errorHanlder:^(NSError *error, BOOL isCanceled) {
NSLog(@"User Canceled Login Process %@",isCanceled ? @"true" :@"false");
NSLog(@"Error Found is %@",error.localizedDescription);
}];
}
```
```
// For Log Out
[SocialHelper socialLogout];
```

For more details please refer to the ExampleSocialSample attached.

### Contributors
***
* Rahul Sharma
* Himanshu Gupta

### Contact Us
***
Get in touch with us with your suggestions, thoughts and queries at engineering@naukri.com

### License
***
Please see [LICENSE.md](LICENSE.md) for details
