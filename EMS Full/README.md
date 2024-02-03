# Employee Management System with User Login

## Setup

The project works right out of the box except the password reset with OTP for a user. For that one will have to use a SMTP server. Here's a list of servers one can use: 
- [Brevo](https://www.brevo.com/) - 300 free e-mails/day
- [MailGun](https://www.mailgun.com/) - 20k e-mails/month with Github student plan
- Gmail - [Tutorial](https://towardsdatascience.com/automate-sending-emails-with-gmail-in-python-449cc0c3c317)

### Configure the .env file with SMTP server details.
Use the template .env.example file included in the src folder to create your own and place the .env file in the src folder

### DB Config
The `config.java` file has constants pointing to locations of the database files. Change them to suit your needs.

### External libraries
They are included in the `lib` folder. The libraries used are:
- [Java Mail](https://javaee.github.io/javamail/)
- [JavaBeans Activation Framework](https://www.oracle.com/java/technologies/downloads.html)