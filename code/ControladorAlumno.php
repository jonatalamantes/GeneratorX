<?php 

    require_once(__DIR__."/Alumno.php");
    require_once(__DIR__."/DatabaseManager.php");
    
    /**
     * Clase para Manipular Objetos del Tipo Alumno
     */
    class ControladorAlumno
    {
        /**
         * Recupera un objeto de tipo Alumno
         */
        static function getSingle($keysValues = array())
        {
            if (!is_array($keysValues) || empty($keysValues))
            {
                return null;
            }
        
            $tableAlumno  = DatabaseManager::getNameTable('TABLE_ALUMNO');
        
            $query     = "SELECT $tableAlumno.*
                          FROM $tableAlumno
                          WHERE ";
        
            foreach ($keysValues as $key => $value)
            {
                $query .= "$tableAlumno.$key = '$value' AND ";
            }
            
            $query = substr($query, 0, strlen($query)-4);
            
            $alumno_simple  = DatabaseManager::singleFetchAssoc($query);
            
            if ($alumno_simple !== NULL)
            {
                $alumnoA = new Alumno();
                $alumnoA->fromArray($alumno_simple);
            }
        
            return $alumnoA;
        }
        /**
         * Obtiene todos los alumnos de la tabla de alumnos
         */
        static function getAll($order = 'id', $begin = 0, $cantidad = 10)
        {
            $tableAlumno  = DatabaseManager::getNameTable('TABLE_ALUMNO');
        
            $query     = "SELECT $tableAlumno.*
                          FROM $tableAlumno
                          WHERE $tableAlumno.activo = 'S'
                          ORDER BY ";
        
            if ($order == 'nombres')
            {
                $query = $query . " $tableAlumno.nombres";
            }
            else if ($order == 'apellidoPaterno')
            {
                $query = $query . " $tableAlumno.apellido_paterno";
            }
            else if ($order == 'apellidoMaterno')
            {
                $query = $query . " $tableAlumno.apellido_materno";
            }
            else if ($order == 'password')
            {
                $query = $query . " $tableAlumno.password";
            }
            else if ($order == 'tipo')
            {
                $query = $query . " $tableAlumno.tipo";
            }
            else
            {
                $query = $query . " $tableAlumno.id DESC";
            }
        
            if ($begin >= 0)
            {
                $query = $query. " LIMIT " . strval($begin * $cantidad) . ", " . strval($cantidad+1);
            }
        
            $arrayAlumnos   = DatabaseManager::multiFetchAssoc($query);
            $alumno_simples = array();
        
            if ($arrayAlumnos !== NULL)
            {
                $i = 0;
                foreach ($arrayAlumnos as $alumno_simple) 
                {
                    if ($i == $cantidad && $begin >= 0)
                    {
                        continue;
                    }
        
                    $alumnoA = new Alumno();
                    $alumnoA->fromArray($alumno_simple);
                    $alumno_simples[] = $alumnoA;
                    $i++;
                }
        
                return $alumno_simples;
            }
            else
            {
                return null;
            }
        }
?>
