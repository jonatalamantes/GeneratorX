<?php 

    /**
     * Class to encrypt dates
     */
    class WatashiEncrypt
    {
        /**
         * Get a Random Char
         * 
         * @return [Char] [Random Char]
         */
        function randomChar()
        {
            $chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

            return $chars[rand(0, strlen($chars)-1)];
        }

        function uniqueKey()
        {
            $now = DateTime::createFromFormat('U.u', microtime(true));
            return WatashiEncrypt::encrypt($now->format("YmdHisu"));
        }

        function shiftChar($char, $pos)
        {
            $chars = "hjTfxIRQXvz29l3d8wnAtNHKLFDqay5C76PeZp0YrbGmo4OWBscViJE1USugkM";

            $posC  = strpos($chars, $char);

            if ($pos !== FALSE)
            {
                $newPos = ($posC + $pos) % strlen($chars);
                return $chars[$newPos];
            }
            else
            {
                return "";
            }
        }

        function unshiftChar($char, $pos)
        {
            $chars = "hjTfxIRQXvz29l3d8wnAtNHKLFDqay5C76PeZp0YrbGmo4OWBscViJE1USugkM";
            $posC  = strpos($chars, $char);

            if ($pos !== FALSE)
            {
                $newPos = ($posC - $pos + strlen($chars)) % strlen($chars);
                return $chars[$newPos];
            }
            else
            {
                return "";
            }
        }

        function encrypt($str = "")
        {
            $strCrypt = "";
            $strTemp = $str;

            //$str must be in format 'YYYY-MM-DD HH:MM:SS'

            for ($i = strlen($strTemp); $i < 19; $i++)
            {
                $strTemp .= "0";
            }

            //parse to: "XX2X0XX1X7XX-X0XX1X-X0X1X 0X0X:0X0:X0X0X"

            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[0], 8);  //2
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[1], 1);  //4
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[2], 15); //7
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[3], 2);  //9
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[4], 9); //12
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[5], 12); //14
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[6], 3); //18
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[7], 19); //19
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[8], 10); //21
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[9], 4); //23
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[10], 16); //25
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[11], 11); //26
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[12], 5); //28
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[13], 18); //30
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[14], 13); //31
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[15], 18); //33
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[16], 6); //34
            $strCrypt .= WatashiEncrypt::randomChar(); 
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[17], 14); //36
            $strCrypt .= WatashiEncrypt::randomChar();
            $strCrypt .= WatashiEncrypt::shiftChar($strTemp[18], 7); //38
            $strCrypt .= WatashiEncrypt::randomChar();

            //Shift the first 7 characters
            $right = substr($strCrypt, 0, strlen($strCrypt)-7);
            $left  = substr($strCrypt, strlen($strCrypt)-7);

            $strCrypt = $left . $right;

            //Split the 10 - 18 chars to begin
            $p1 = substr($strCrypt, 0, 10);
            $p2 = substr($strCrypt, 10, 7);
            $p3 = substr($strCrypt, 17);

            $strCrypt = $p2 . $p1 . $p3;

            //Split the 20 - 27 chars to end
            $p1 = substr($strCrypt, 0, 20);
            $p2 = substr($strCrypt, 20, 7);
            $p3 = substr($strCrypt, 27);            

            $strCrypt = $p1 . $p3 . $p2;

            return $strCrypt;
        }   

        function decrypt($str = "")
        {
            $strTemp    = $str;
            $strDecrypt = "";

            for ($i = strlen($strTemp); $i < 39; $i++)
            {
                $strTemp .= "0";
            }

            //Deplit the 20 - 27 chars to end
            $p1 = substr($strTemp, 0, 20);
            $p3 = substr($strTemp, 20, 13);
            $p2 = substr($strTemp, 33);

            $strTemp = $p1 . $p2 . $p3;

            //Deplit the 10 - 18 chars to end
            $p2 = substr($strTemp, 0, 7);
            $p1 = substr($strTemp, 7, 10);
            $p3 = substr($strTemp, 17);            

            $strTemp = $p1 . $p2 . $p3;

            //Deshift the first 7 characters
            $right = substr($strTemp, 0, 7);
            $left  = substr($strTemp, 7);

            $strTemp = $left . $right;

            //unparse to: "XX2X0XX1X7XX-X0XX1X-X0X1X 0X0X:0X0:X0X0X"
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[2], 8);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[4], 1);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[7], 15);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[9], 2);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[12], 9);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[14], 12);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[17], 3);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[19], 19);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[21], 10); 
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[23], 4);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[25], 16);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[26], 11);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[28], 5);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[30], 18);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[31], 13);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[33], 18);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[34], 6);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[36], 14);
            $strDecrypt .= WatashiEncrypt::unshiftChar($strTemp[38], 7);

            return $strDecrypt;
        }
    }

 ?>