<?php
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

// Load Composer's autoloader
require 'vendor/autoload.php';

//connection to db
require 'conn.php';

// Instantiation and passing `true` enables exceptions
$mail = new PHPMailer(true);
$username = $_POST["username"];
$Reciever = "";

if($conn){		
				echo $username;
			
				$q1 = "select Email from users where Username like '$username'";
				$result = mysqli_query($conn,$q1);
				
				while($row = mysqli_fetch_array($result)) {
					$Reciever= $row['Email']; // Print a single column data
					echo $Reciever;
				}
			
		}


try {
    //Server settings
    $mail->SMTPDebug = SMTP::DEBUG_SERVER;                      // Enable verbose debug output
    $mail->isSMTP();                                            // Send using SMTP
    $mail->Host       = 'smtp.gmail.com';                    // Set the SMTP server to send through
    $mail->SMTPAuth   = true;                                   // Enable SMTP authentication
    $mail->Username   = 'safestreet2019@gmail.com';                     // SMTP username
    $mail->Password   = 'safestreet!tah';                               // SMTP password
    $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;         // Enable TLS encryption; `PHPMailer::ENCRYPTION_SMTPS` also accepted
    $mail->Port       = 587;                                    // TCP port to connect to

    //Recipients
    $mail->setFrom('safestreet2019@gmail.com', 'Demo Email');
    $mail->addAddress("$Reciever", 'Taras');     // Add a recipient
  

    // Attachments
   $mail->addAttachment('violationdata.xlsx');         // Add attachments
       

    // Content
    $mail->isHTML(true);                                  // Set email format to HTML
    $mail->Subject = 'Your Requested Data';
    $mail->Body    = 'Attached is Excel Sheet with the requested data';
    $mail->AltBody = 'Attached is Excel Sheet with the requested data';

    $mail->send();
    echo 'Message has been sent';
} catch (Exception $e) {
    echo "Message could not be sent. Mailer Error: {$mail->ErrorInfo}";
}