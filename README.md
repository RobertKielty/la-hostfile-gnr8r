### Linux Academy local hostfile generator

[Linux Academy](https://www.linuxacademy.com) is an online learning platform dedicated to teaching Cloud Technologies.

As a Linux Academy subscriber, you can start up machines in the cloud to gain hands on experience as you work through 
the courses.

Each time you start up your cloud machines on Linux Academy they are assigned new public IP addresses.

This application generates a sed script that will edit your hosts file to replace the old IP addresses with new ones.

The app uses [Selenide](https://selenide.org/), a test automation framework based on Selenium which allows the app to 
drive a browser to login and traverse the Linux Academy web interface to gather the public IP address of all 
running machines. 

### Assumptions 

The following assumptions are made about your setup and really reflect my current setup a laptop running linux.

 - You have a means of running [sed](https://twww.gnu.org/software/sed/manual/sed.html)
 - You have manually logged in to Linux Academy to start the machines you need for your learning session
 - You are logging to Linux Academy using a linked Google account
 - The generated sed script relies on the fact that you have manually added the host names to your /etc/hosts file
 
 So there are certainly improvements to be made on this implementation. 
 
### To run this app

```
$ git clone 
$ cd la-hosts-generator

# Run the test which will retrieve the latest public IP addresses of your running Linux Academy Cloud hosts 
$ ./gradlew -q -Dlinuxacademy.username=YOUR_GMAIL_EMAIL_ADDR -Dlinuxacademy.password=YOUR_GMAIL_PASSWORD

# Take a copy or your current hosts file into this directory, remember it assumes that there are entries for host 
# we are about to edit with the generated sed script 
$ cp /etc/hosts .

# Run GNU sed with extend regex's 
sed -r -f la_hosts.sed -i.orig hosts && diff hosts hosts.orig
```






