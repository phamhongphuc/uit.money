 "/mnt/c/Program Files/RedHat/java-1.8.0-openjdk-1.8.0.161-1/bin/keytool.exe" -exportcert -alias keystore_alias -keystore keystore.jks | openssl sha1 -binary | openssl base64

 "/mnt/c/Program Files/RedHat/java-1.8.0-openjdk-1.8.0.161-1/bin/keytool.exe" -exportcert -alias androiddebugkey -keystore "mnt/c/Users/phamhongphuc/.android/debug.keystore" | openssl sha1 -binary | openssl base64
