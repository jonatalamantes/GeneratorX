<?php 
    
    require_once("SessionManager.php");
    require_once("ConfigDatabase.php");
    require_once("Mysqldump.php");

    /**
    * Class for manipulate the Database
    * 
    * @author Jonathan Sandoval <jonathan_s_pisis@yahoo.com.mx>
    */
    class DatabaseManager
    {
        static private $mysqli_obj = NULL;

        /**
        * Create a new connection with the database. If exist don't do anything
        *
        * @author Jonathan Sandoval <jonathan_s_pisis@yahoo.com.mx>
        **/
        static function connectDB()
        {
            if (!isset(self::$mysqli_obj) || is_NULL(self::$mysqli_obj))
            {                
                $nombreDB = constant('DATABASE');
                $usuario  = constant('USER');
                $host     = constant('HOST');
                $clave    = constant('PASSWORD');

                self::$mysqli_obj = new mysqli($host, $usuario, $clave, $nombreDB);

                /* check connection */
                if (self::$mysqli_obj->connect_errno) 
                {
                    printf("Connect failed: %s\n", self::$mysqli_obj->connect_error);
                    exit();
                }
                else
                {
                    self::$mysqli_obj->set_charset("utf8");
                }
            }
        }

        /**
         * Get the last error from database
         *
         * @author Jonathan Sandoval <jonathan_s_pisis@yahoo.com.mx>
         * @return String Description of last error if exist
         */
        static function getLastError()
        {
            return self::$mysqli_obj->error;
        }

        /**
        * Quit the actual Database Connection
        *
        * @author Jonathan Sandoval    <jonathan_s_pisis@hotmail.com>
        **/
        static function disconnectDB()
        {
            self::$mysqli_obj = NULL;
        }

        /**
        * Check the input string for not have characters or commands that damage the system
        *
        * @author Jonathan Sandoval    <jonathan_s_pisis@hotmail.com>
        * @param  string[string]       string to be evaluated
        * @return string[newString]    strint without that characters
        **/
        static function escapeString($string = "")
        {
            //Save the Query
            //self::logActivity($string);

            $posBad = -1;
            $newString = $string;

            if (strpos($newString, ";") !== FALSE)
            {
                $newString = substr($newString, 0, strpos($newString, ";"));
            }

            //Quit the word 'DROP' from the String
            do
            {
                $posBad = mb_strpos($newString, "DROP ");

                if ($posBad !== false)
                {
                    $newString = substr($newString, 0, $posBad) . substr($newString, $posBad + 5);    
                }
            }
            while($posBad !== false);

            //Quit the word 'USE' from the String
            do
            {
                $posBad = mb_strpos($newString, "USE ");

                if ($posBad !== false)
                {
                    $newString = substr($newString, 0, $posBad) . substr($newString, $posBad + 4);
                }

            }
            while($posBad !== false);

            //Quit the word 'DELETE' without 'WHERE' from the String
            do
            {
                $posBad = mb_strpos($newString, "DELETE ");

                if ($posBad !== false)
                {
                    $tempString = substr($newString, $posBad);
                    $posWHERE   = mb_strpos($tempString, "WHERE");

                    if ($posWHERE === false) //DELETE' without 'WHERE' found
                    {
                        $newString = substr($newString, 0, $posBad).substr($newString, $posBad + 7);
                    }
                    else
                    {
                        break;
                    }
                }
            }
            while($posBad !== false);
                        
            SessionManager::setLastQuery($newString);

            return $newString;
        }

        /**
        * Exect one instruction with the database connection
        *
        * @author Jonathan Sandoval       <jonathan_s_pisis@hotmail.com>
        * @param  string $string          String yo exect in the database
        * @return mysqli_ress[resultado]  result object
        **/
        static function simpleQuery($string = "")
        {
            $newString = self::escapeString($string);
            $ress = self::$mysqli_obj->query($newString);

            if (self::$mysqli_obj->error !== "")
            {
                echo "Error en consulta: ", self::$mysqli_obj->error, "<br>";
            }

            return $ress;
        }

        /**
        * return a Array of Asociative Arrays after exect the query
        *
        * @author Jonathan Sandoval       <jonathan_s_pisis@hotmail.com>
        * @param  string $query           String yo exect in the database
        * @return Array[arrayAssoc]       Array of Asociative Arrays from Database
        **/
        static function multiFetchAssoc($query = "", $limit = 0)
        {
            $newString = self::escapeString($query);
            $ress = self::$mysqli_obj->query($newString);   

            $arrayAssoc = array();         

            if ($ress === false)
            {
                return null;
            }

            if ($limit > 0)
            {
                $i = 0;

                while ($row = $ress->fetch_assoc() && $i < $limit)
                {
                    $arrayAssoc[$i] = $row;
                    $i++;
                }
            }
            else
            {
                while ($row = $ress->fetch_assoc())
                {
                    $arrayAssoc[] = $row;
                }
            }

            if (self::$mysqli_obj->error !== "")
            {
                echo "Error en consulta: ", self::$mysqli_obj->error, "<br>";
            }

            return $arrayAssoc;
        }

        /**
        * return a Asociative Array after exect the query
        *
        * @author Jonathan Sandoval       <jonathan_s_pisis@hotmail.com>
        * @param  string $query           String yo exect in the database
        * @return Array[row]              Array after Fetch or null
        **/
        static function singleFetchAssoc($query = "")
        {
            $newString = self::escapeString($query);
            $ress = self::$mysqli_obj->query($newString);

            if ($ress === false)
            {
                return null;
            }
            
            if (self::$mysqli_obj->error !== "")
            {
                echo "Error en consulta: ", self::$mysqli_obj->error, "<br>";
            }

            if ($row = $ress->fetch_assoc())
            {
                return $row;
            }
            else
            {
                return null;
            }
        }


        /**
        * return if one query only affect one object
        *
        * @author Jonathan Sandoval       <jonathan_s_pisis@hotmail.com>
        * @param  string $query           String yo exect in the database
        * @return boolean                 Array after Fetch or null
        **/
        static function singleAffectedRow($query = "")
        {
            $newString = self::escapeString($query);
            $ress = self::$mysqli_obj->query($newString);

            if ($ress === false)
            {
                return null;
            }

            if (self::getAffectedRows() == 1)
            {
                return true;
            }
            else
            {
                return false;
            }
        }


        /**
        * Get the amount of rows affected by the last database operation
        *
        * @author Jonathan Sandoval <jonathan_s_pisis@yahoo.com.mx>
        * @return int[afected]      Number of rows affected
        **/
        static function getAffectedRows()
        {
            $afected = self::$mysqli_obj->affected_rows;
            return $afected;
        }

        /**
        * Get the total registries affected without limit
        *
        * @author Jonathan Sandoval <jonathan_s_pisis@yahoo.com.mx>
        * @return int[afected]      Number of rows affected
        **/
        static function registriesAffectedLastQuery()
        {
            $query = SessionManager::getLastQuery();

            $posFrom  = strpos($query, "FROM");
            $posOrder = strpos($query, "LIMIT");

            if ($posOrder !== FALSE && $posFrom !== FALSE)
            {
                $query = substr($query, $posFrom, -(strlen($query) - $posOrder));
            }
            else if ($posFrom !== FALSE)
            {
                $query = substr($query, $posFrom);
            }

            $query = "SELECT COUNT(*) AS Total " . $query;

            $x = self::singleFetchAssoc($query);
            return intval($x["Total"]);
        }

        /**
         * Return the table name 
         *
         * @author Jonathan Sandoval <jonathan_s_pisis@yahoo.com.mx>
         * @param  string $name table name in mayus case
         * @return string       name of that table in the database       
         */
        static function getNameTable($name = "")
        {
            return constant("$name");
        }

        /** 
        * Sort one associative array by one subkey
        *
        * @author Jonathan Sandoval    <jonathan_s_pisis@hotmail.com>
        * @param  &array        array reference
        * @param  string        subkey name
        * @param  typeSort      constant of the type of sort
        */
        static function sortBySubkey(&$array = null, $subkey = "", $sortType = SORT_ASC) 
        {
            if ($array !== NULL)
            {
                foreach ($array as $subarray) 
                {
                    $keys[] = $subarray[$subkey];
                }

                array_multisort($keys, $sortType, $array);
            }
        }

        /**
         * Get the surplus from the URL
         *
         * @author Jonathan Sandoval    <jonathan_s_pisis@hotmail.com>
         * @return String               example if the string is: 
         *                              "http//localhost/hello.php?type=1&" 
         *                              return "?type=1&"
         */
        static function getSurplusURL()
        {
            $size = strlen(htmlentities($_SERVER['PHP_SELF']));
            $url = substr($_SERVER['REQUEST_URI'], $size);

            return $url;
        }

        /**
        * Transform one database date to one single date
        *
        * @author Jonathan Sandoval <jonathan_s_pisis@yahoo.com.mx>
        * @param  string  $date    The date previous (0000-00-00)
        * @return string  $myDate  The date result   (00/00/0000)
        **/
        static function databaseDateToSingleDate($date = "0000-00-00")
        {
            if ($date === "" || $date === NULL)
            {
                return "";
            }

            $data    = array();
            $surplus = "";

            for ($i = 0; $i < strlen($date); $i++) 
            { 
                if ($date[$i] == '-')
                {
                    $data[] = $surplus;
                    $surplus = "";
                }
                else
                {
                    $surplus = $surplus . $date[$i];
                }
            }

            $data[] = $surplus;

            $myDate = $data[2] . "/" . 
                      $data[1] . "/" . 
                      $data[0];

            return $myDate;
        }

        /**
        * Transform one single date to one database date
        *
        * @author Jonathan Sandoval <jonathan_s_pisis@yahoo.com.mx>
        * @param  string  $date    The date previous (00/00/0000)
        * @return string  $myDate  The date result   (0000-00-00)
        **/
        static function singleDateToDatabaseDate($date = "00/00/0000")
        {
            if ($date === "" || $date === NULL)
            {
                return "";
            }

            $data    = array();
            $surplus = "";

            for ($i = 0; $i < strlen($date); $i++) 
            { 
                if ($date[$i] == '/')
                {
                    $data[] = $surplus;
                    $surplus = "";
                }
                else
                {
                    $surplus = $surplus . $date[$i];
                }
            }

            $data[] = $surplus;

            $myDate = $data[2] . "-" . 
                      $data[1] . "-" . 
                      $data[0];

            return $myDate;        
        }

        static function simpleParse($cad = "", $tipo = "", $default = 'N', $reference = NULL)
        {
            if ($tipo == "int")
            {
                $letras = '01234567890+-.';

                if ($reference !== NULL)
                {
                    $letras = $reference;
                }

                $newCad = "";

                for ($i = 0; $i < strlen($cad); $i++)
                {
                    if (strpos($letras, $cad[$i]) !== FALSE)
                    {
                        $newCad .= $cad[$i];
                    }
                }

                if ($newCad == "")
                {
                    return intval($default);
                }
                else
                {
                    return intval($newCad);
                }
            }
            else if ($tipo == "bool")
            {
                $letras = 'SN';

                if ($reference !== NULL)
                {
                    $letras = $reference;
                }

                $newCad = "";

                for ($i = 0; $i < strlen($cad); $i++)
                {
                    if (strpos($letras, $cad[$i]) !== FALSE)
                    {
                        $newCad .= $cad[$i];
                    }
                }

                if ($newCad == "")
                {
                    return "'" . $default . "'";
                }
                else
                {
                    return "'" . $newCad . "'";
                }
            }
            else if ($tipo == "FK")
            {
                $letras = '01234567890';

                if ($reference !== NULL)
                {
                    $letras = $reference;
                }

                $newCad = "";

                for ($i = 0; $i < strlen($cad); $i++)
                {
                    if (strpos($letras, $cad[$i]) !== FALSE)
                    {
                        $newCad .= $cad[$i];
                    }
                }

                if ($newCad == "")
                {
                    return $default;
                }
                else
                {
                    return $newCad;
                }
            }
            else
            {
                $letras = 'ABCDEFGHIJKLMNOPQRSTUVWXYZÑÁÉÍÓÚŮ abcdefghijklmnopqrstuvwxyzñáéíóúů01234567890#$%&/.,!?¿*+-:_@\n';

                if ($reference !== NULL)
                {
                    $letras = $reference;
                }

                $newCad = "";

                for ($i = 0; $i < strlen($cad); $i++)
                {
                    if (strpos($letras, $cad[$i]) !== FALSE)
                    {
                        $newCad .= $cad[$i];
                    }
                }

                if ($newCad == "")
                {
                    return "'" . $default . "'";
                }
                else
                {
                    return "'" . $newCad . "'";
                }
            }

            if ($str == NULL)
            {
                return "NULL";
            }
        }

        /**
        * Create a new backup of the database if exist
        *
        * @author Jonathan Sandoval <jonathan_s_pisis@yahoo.com.mx>
        **/
        static function createBackup()
        {            
            $nombreDB = constant('DATABASE');
            $usuario  = constant('USER')
            $host     = constant('HOST');
            $clave    = constant('PASSWORD')
            $tables   = '*';
            $path = __DIR__."/../".date('y-m-d').".sql";

            if (!file_exists($path))
            {
                //get the backups amount
                exec("dir ".__DIR__."/Backups/", $output, $return);

                if (is_array($output) && sizeof($output) > 0)
                {
                    $files = array();

                    foreach ($output as $singleOutput) 
                    {
                        $aux = "";
                        $actual = "";

                        for ($i = 0; $i < strlen($singleOutput); $i++)
                        {
                            $actual = substr($singleOutput, $i, 1);

                            if ($actual === ' ')
                            {
                                if ($aux !== "")
                                {
                                    $files[] = $aux;
                                }

                                $aux = "";
                            }
                            else
                            {
                                $aux = $aux . $actual;
                            }
                        }

                        if ($aux !== "")
                        {
                            $files[] = $aux;
                        }
                    }

                    while (sizeof($files) > 4) //Delete the older files
                    {
                        $exect = "rm '".__DIR__."/Backups/".$files[0]."'";
                        exec($exect, $output);
                        array_shift($files);
                    }
                }

                //Create the current Backup
                try
                {
                    $stringHost = "mysql:host=$host;dbname=$nombreDB";
                    $dump = new Ifsnop\Mysqldump\Mysqldump($stringHost, $usuario, $clave);
                    $dump->start($path);
                }
                catch (\Exception $e) 
                {
                    echo 'mysqldump-php error: ' . $e->getMessage();
                }
            }
        }
    
        /**
         * Gets the value of mysqli_obj.
         *
         * @return mixed
         */
        static public function getMysqliObj()
        {
            return self::$mysqli_obj;
        }

        /**
         * Sets the value of mysqli_obj.
         *
         * @param mixed $mysqli_obj the mysqli obj
         *
         * @return self
         */
        public function setMysqliObj($mysqli_obj)
        {
            self::$mysqli_obj = $mysqli_obj;

            return $this;
        }
    }

    DatabaseManager::connectDB();
    //DatabaseManager::createBackup();

 ?>