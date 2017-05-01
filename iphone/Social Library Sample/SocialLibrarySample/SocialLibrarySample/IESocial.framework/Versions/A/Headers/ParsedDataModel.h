//
//  ParsedDataModel.h
//  SocialLibrary
//
//  Created by RahulSharma on 3/21/17.
//  Copyright © 2017 INFOEDGE INDIA LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ParsedDataModel : NSObject

// User Id from fb / gmail
@property (nonatomic , strong) NSString *userId;
//Auth Token from fb / gmail
@property (nonatomic , strong) NSString *userTokenKey;
// either @"fb" or "@"gp for server"
@property (nonatomic , strong) NSString *socialKey;
// Expired Date of token from fb / gmail
@property (nonatomic , strong) NSDate *tokenExpiredDate;

@property (nonatomic, strong) NSString * email;

@property (nonatomic, strong) NSString * name;

@property (nonatomic , strong) NSURL *imageUrl;

@property (nonatomic , strong) id unParsedFieldDict;

@end
