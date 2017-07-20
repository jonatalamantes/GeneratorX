<?php 

    require_once(__DIR__."/../Backend/Controlers/DatabaseManager.php");

    $sql = "SHOW TABLES";
    $tables = DatabaseManager::multiFetchAssoc($sql);

    $contadorTablas = 1;
    $gxFile = "";
    foreach ($tables as $key => $value) 
    {
        $t =  $value["Tables_in_ExpedienteNuevo"];

        $gxFile .= $contadorTablas . "*";
        $gxFile .= $t . "*";

        $sql2 = "DESCRIBE $t";
        $desc = DatabaseManager::multiFetchAssoc($sql2);

        $contadorAttr = 1;
        foreach ($desc as $key => $attr) 
        {
            $gxFile .= $contadorAttr . "|";
            $gxFile .= $attr["Field"] . "|";
            $gxFile .= "0|";
            $gxFile .= "|";
            $gxFile .= "|";

            if ($attr["Key"] == "PRI")
            {
                $gxFile .= "1|";
            }
            else
            {
                $gxFile .= "0|";
            }

            if ($attr["Key"] == "MUL")
            {
                $gxFile .= "1|";
            }
            else
            {
                $gxFile .= "0|";
            }

            if ($attr["Null"] == "Yes")
            {
                $gxFile .= "1|";
            }
            else
            {
                $gxFile .= "0|";
            }

            if ($attr["Field"] == "id"             || $attr["Field"] == "uid" || 
                $attr["Field"] == "fecha_registro" || $attr["Field"] == "activo")
            {
                $gxFile .= "0|";   
            }
            else
            {
                $gxFile .= "1|";
            }

            $gxFile .= "&";
            $contadorAttr++;
        }

        $gxFile .= "*0*$";
        $contadorTablas++;
    }

    $f = fopen("DB.gx", "w+");
    fwrite($f, $gxFile);
    fclose($f);


 ?>