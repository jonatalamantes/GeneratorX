<?php 

    /**
    * Parser Util
    */
    class ParserUtil
    {
        static function parseInt($cad = "", $default = '0', $reference = NULL)
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

        static function parseFloat($cad = "", $default = '0', $reference = NULL)
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
                return floatval($default);
            }
            else
            {
                return floatval($newCad);
            }
        }

        static function parseBool($cad = "", $default = 'N', $reference = NULL)
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

        static function parseNaturalInt($cad = "", $default = '0', $reference = NULL)
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

        static function parseTime($cad = "", $default = '0', $reference = NULL)
        {
            $letras = '1234567890:/';

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

        static function parseString($cad = "", $default = '0', $reference = NULL)
        {
            if ($cad == NULL)
            {
                return "NULL";
            }

            $letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZÑÁÉÍÓÚÜ abcdefghijklmnopqrstuvwxyzñáéíóúü01234567890#$%&/.,!?¿*+-:_@\n";

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
        
        static function simpleParse($cad = "", $tipo = "", $default = 'N', $reference = NULL)
        {
            if ($tipo == "int")
            {
                return ParserUtil::parseInt($cad);
            }
            else if ($tipo == "bool")
            {
                return ParserUtil::parseBool($cad, $default, $reference);
            }
            else if ($tipo == "FK")
            {
                return ParserUtil::parseNaturalInt($cad, $default, $reference);
            }
            else if ($tipo == "time")
            {
                return ParserUtil::parseTime($cad, $default, $reference);
            }
            else
            {
                return ParserUtil::parseString($cad, $default, $reference);
            }
        }
    }

    /*var_dump(ParserUtil::parseInt("-10.25"));
    var_dump(ParserUtil::parseFloat("-10.25"));
    var_dump(ParserUtil::parseNaturalInt("-10.25"));
    var_dump(ParserUtil::parseTime("10/1a/2015"));
    var_dump(ParserUtil::parseTime("a0:00:00"));
    var_dump(ParserUtil::parseBool("S"));
    var_dump(ParserUtil::parseString("Hola mundo ó<hol> %&"));*/

 ?>