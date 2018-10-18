### Linux Academy local hostfile generator

*Small project that I used to learn some Selenide/Selenium browser automation. Might be useful for other Linux Academy Subscribers*

[Linux Academy](https://www.linuxacademy.com) is an online learning platform dedicated to teaching Cloud Technologies.

As a Linux Academy subscriber, you can start up machines in the cloud to gain hands on experience as you work through
the courses.

Each time you start up your cloud machines on Linux Academy they are assigned new public IP addresses.

This application generates a new hosts file, `hosts.latest` in the top level directory of the project with the latest IP addresses of the running machines on your Linux Academy account.

The app uses [Selenide](https://selenide.org/), a test automation framework based on Selenium which allows the app to drive a browser to login and traverse the Linux Academy web interface to gather the public IP address of all
running machines.

### Storing an encrypted copy of your Linux Academy credentials
This uses the [gradle credentials plugin](https://github.com/etiennestuder/gradle-credentials-plugin) to store an encrypted copy of your Linux Academy credentials.

You can set them up as follows:
```
gradle addCredentials --key lauser --value 'YOUR_LA_USERNAME' -PcredentialsPassphrase=A_PASSWORD_FOR_CREDENTIALS_STORE
gradle addCredentials --key lapassword --value 'YOUR_LA_PASSWORD' -PcredentialsPassphrase=A_PASSWORD_FOR_CREDENTIALS_STORE
```

If using bash you can avoid interpretation of special characters by enclosing the credentials in single quotes.

This will place your credentials in a file encrypted using PBKDF2WithHmacSHA1. You can clear down this file and remove individual credentials from this store. See the above plugin and its code for more details.

### Assumptions

The following assumptions are made about your setup and really reflect my current setup a laptop running linux.

 - You have manually logged in to Linux Academy to start the machines you need for your learning session
 - You are logging in to Linux Academy using a linked Google account - It would't be too hard to improve this for other login options.

### To run this app

```
$ git clone git@github.com:RobertKielty/la-hostfile-gnr8r.git
$ cd la-hostfile-gnr8r

# Run the test which will retrieve the latest public IP addresses of your running Linux Academy Cloud hosts
$ ./gradlew -q -PcredentialsPassphrase=A_PASSWORD_FOR_CREDENTIALS_STORE

# Review the changes
diff hosts.latest /etc/hosts

# If all is well, update your /etc/hosts file
sudo cp hosts.latest /etc/hosts
```
