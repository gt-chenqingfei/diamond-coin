## keysotre
```
keytool.exe -list -keystore ediamond.key -v

输入密钥库口令:

密钥库类型: JKS
密钥库提供方: SUN

您的密钥库包含 1 个条目

别名: ediamond
创建日期: 2017-11-11
条目类型: PrivateKeyEntry
证书链长度: 1
证书[1]:
所有者: CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown
发布者: CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown
序列号: 2018d9ec
有效期开始日期: Sat Nov 11 19:46:38 CST 2017, 截止日期: Wed Mar 29 19:46:38 CST 2045
证书指纹:
         MD5: 1F:07:AE:46:3A:97:F8:23:2E:00:66:DA:F7:EC:C5:D3
         SHA1: FB:B2:E8:90:D5:0B:0F:80:A4:D7:64:31:4E:5B:EB:40:07:AE:CA:CB
         SHA256: 4E:15:14:0F:C5:C7:21:E9:07:FA:46:A5:71:F0:9B:31:0B:5F:88:7C:64:5C:14:19:5E:9B:2B:4B:C4:EE:00:A9
         签名算法名称: SHA256withRSA
         版本: 3

扩展:

#1: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 89 CF 7F 0A 4E 04 03 94   5C CE 8B D5 63 2E E5 0B  ....N...\...c...
0010: 56 99 AB 32                                        V..2
]
]



*******************************************
*******************************************
```


## facebook 

```aidl
keytool -exportcert -alias ediamond -keystore ediamond.key | openssl sha1 -binary | openssl base64
输入密钥库口令:  ediamond
+7LokNULD4Ck12QxTlvrQAeuyss=
```