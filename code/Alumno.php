<?php 

    /**
    * Clase para el Alumno
    */
    Class Alumno
    {
        private $id;
        private $nombres;
        private $apellidoPaterno;
        private $apellidoMaterno;
        private $password;
        private $activo;
        private $fechaRegistro;
        private $tipo;

        /**
         * Constructor de la Clase
         */
        function __construct($id = "", $nombres = "", $apellido_paterno = "", 
                            $apellido_materno = "", $password = "", $activo = "", 
                            $fecha_registro = "", $tipo = "")
        {
            $this->id              = $id;
            $this->nombres         = $nombres;
            $this->apellidoPaterno = $apellido_paterno;
            $this->apellidoMaterno = $apellido_materno;
            $this->password        = $password;
            $this->activo          = $activo;
            $this->fechaRegistro   = $fecha_registro;
            $this->tipo            = $tipo;
        }
        
        /**
         * Retorna un Array del Objeto
         * 
         * @return [array] [Array Asociativo Resultante]
         */
        public function toArray()
        {
            $array = array();
        
            if ($this !== null)
            {
                $array["id"]               = $this->getId();
                $array["nombres"]          = $this->getNombres();
                $array["apellido_paterno"] = $this->getApellidoPaterno();
                $array["apellido_materno"] = $this->getApellidoMaterno();
                $array["password"]         = $this->getPassword();
                $array["activo"]           = $this->getActivo();
                $array["fecha_registro"]   = $this->getFechaRegistro();
                $array["tipo"]             = $this->getTipo();
            }
        
            return $array;
        }
        
        /**
         * Toma los datos de un Array para el Objeto
         * 
         * @param  array  $array [Array Entrante]
         */
        public function fromArray($array = array())
        {
            if (!empty($array))
            {
                $this->setId($array["id"]);
                $this->setNombres($array["nombres"]);
                $this->setApellidoPaterno($array["apellido_paterno"]);
                $this->setApellidoMaterno($array["apellido_materno"]);
                $this->setPassword($array["password"]);
                $this->setActivo($array["activo"]);
                $this->setFechaRegistro($array["fecha_registro"]);
                $this->setTipo($array["tipo"]);
            }
        }
        
        /**
         * Calculo para saber que tan diferente es un objeto de otro
         * 
         * @param  Alumno $obj [Objeto con el que se comparara]
         * @return [float]     [Disimilitud entre los dos objetos]
         */
        public function disimilitud($obj = null)
        {
            if ($obj === null)
            {
                return -1;
            }
        
            $disimilitud = 0;
            $numerador   = 0;
            $denominador = 0;
            
            if ($obj->getNombres() != $this->getNombres())
            {
                $numerador += 1;
            }
        
            $denominador += 1;
        
            if ($obj->getApellidoPaterno() != $this->getApellidoPaterno())
            {
                $numerador += 1;
            }
        
            $denominador += 1;
        
            if ($obj->getApellidoMaterno() != $this->getApellidoMaterno())
            {
                $numerador += 1;
            }
        
            $denominador += 1;
        
            if ($obj->getPassword() != $this->getPassword())
            {
                $numerador += 1;
            }
        
            $denominador += 1;
        
            if ($obj->getTipo() != $this->getTipo())
            {
                $numerador += 1;
            }
        
            $denominador += 1;
        
            $disimilitud = (float)($numerador/$denominador);
            return $disimilitud;
        }
        
        /**
         * Gets the value of id
         */
        public function getId()
        {
            return $this->id;
        }
        
        /**
         * Sets the value of id
         */
        public function setId($id)
        {
            $this->id = $id;
        }
        
        /**
         * Gets the value of nombres
         */
        public function getNombres()
        {
            return $this->nombres;
        }
        
        /**
         * Sets the value of nombres
         */
        public function setNombres($nombres)
        {
            $this->nombres = $nombres;
        }
        
        /**
         * Gets the value of apellidoPaterno
         */
        public function getApellidoPaterno()
        {
            return $this->apellidoPaterno;
        }
        
        /**
         * Sets the value of apellidoPaterno
         */
        public function setApellidoPaterno($apellidoPaterno)
        {
            $this->apellidoPaterno = $apellidoPaterno;
        }
        
        /**
         * Gets the value of apellidoMaterno
         */
        public function getApellidoMaterno()
        {
            return $this->apellidoMaterno;
        }
        
        /**
         * Sets the value of apellidoMaterno
         */
        public function setApellidoMaterno($apellidoMaterno)
        {
            $this->apellidoMaterno = $apellidoMaterno;
        }
        
        /**
         * Gets the value of password
         */
        public function getPassword()
        {
            return $this->password;
        }
        
        /**
         * Sets the value of password
         */
        public function setPassword($password)
        {
            $this->password = $password;
        }
        
        /**
         * Gets the value of activo
         */
        public function getActivo()
        {
            return $this->activo;
        }
        
        /**
         * Sets the value of activo
         */
        public function setActivo($activo)
        {
            $this->activo = $activo;
        }
        
        /**
         * Gets the value of fechaRegistro
         */
        public function getFechaRegistro()
        {
            return $this->fechaRegistro;
        }
        
        /**
         * Sets the value of fechaRegistro
         */
        public function setFechaRegistro($fechaRegistro)
        {
            $this->fechaRegistro = $fechaRegistro;
        }
        
        /**
         * Gets the value of tipo
         */
        public function getTipo()
        {
            return $this->tipo;
        }
        
        /**
         * Sets the value of tipo
         */
        public function setTipo($tipo)
        {
            $this->tipo = $tipo;
        }
        
    }
?>
