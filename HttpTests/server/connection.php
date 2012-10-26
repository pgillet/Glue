<?php  
//$un=$_POST['username'];  
//$pw=$_POST['password']; 
//$un='Gregouze31';
//$pw='javea2012';
//connect to the db  
//$user = 'badmintoehjoomla';  
//$pswd = '234kalum';  
//$db = 'mysql51zfs-42.perso';
//$db = 'badmintoehjoomla';  
//$conn = mysql_connect('mysql51-42.perso', $user, $pswd); 
//$conn = mysql_connect('mysql51zfs-42.perso', $user, $pswd);
//echo $conn;
//mysql_select_db($db, $conn);  
//run the query to search for the username and password the match  
//$query = 'SELECT * FROM jnew_users WHERE username = \''.$un.'\' AND password = \''.$pw.'\'';
//$query = 'SELECT * FROM jnew_users WHERE username = \''.$un.'\''; 
//echo password($pwd);
//$result = mysql_query($query) or die('Unable to verify user because : ' . mysql_error());  
//this is where the actual verification happens  
//if (mysql_num_rows($result) > 0)  
// ( $un == “ajay” && $pw == “ajay”)  
//echo 1;  // for correct login response  
//else  
//echo 0; // for incorrect login response

define('_JEXEC', 1 );
define('DS', DIRECTORY_SEPARATOR);
define('JPATH_BASE', dirname(__FILE__) . DS . '..'); // assuming we are in the authorisation plugin folder and need to go up 3 steps to get to the Joomla root

require_once (JPATH_BASE .DS. 'includes' .DS. 'defines.php');
require_once (JPATH_BASE .DS. 'includes' .DS. 'framework.php');
require_once (JPATH_BASE .DS. 'libraries' .DS. 'joomla'. DS. 'user' .DS. 'authentication.php');

$mainframe =& JFactory::getApplication('site');
$mainframe->initialise();

$username = JRequest::getVar('username');
$password = JRequest::getVar('password');

$credentials = array(
    'username' => $username,
    'password' => $password);

$options = array();

$authenticate = JAuthentication::getInstance();
$response   = $authenticate->authenticate($credentials, $options);

if ($response->status === JAUTHENTICATE_STATUS_SUCCESS) {
    echo('1');
} else {
	echo('0');
}
 
?>  