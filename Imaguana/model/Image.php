<?php
Class Image { 
    public $id;
    public $name;
    public $title; 
    public $path; 
    public $description;
    public $category;
    
    function __construct($id) {
      #$db['default']['db_debug'] = FALSE;
      #$db['default']['host'] = "localhost";
      #mysql_connect("sfsuswe.com", "mchuang", "GQaUAwTu59bUV2es") or die(mysql_error());
      #mysql_select_db("student_mchuang") or die(mysql_error());
      
       mysql_connect("sfsuswe.com", "twinpair", "csc2016") or die(mysql_error());
       mysql_select_db("student_twinpair") or die(mysql_error());
      
      $query = mysql_query("SELECT * "
                                . "FROM Search "
                                . "WHERE id = '$id' "
                                ) or die("Error");
      if($row = mysql_fetch_array($query)) {
        $this->id = $row['id'];
        $this->name = $row['name'];
        $this->title = $row['title'];
        $this->image = $row['image'];
        $this->description = $row['description'];
        $this->category = $row['category'];
      }
      
    }
    
    function Read($id) {
        mysql_connect("sfsuswe.com", "mchuang", "GQaUAwTu59bUV2es") or die(mysql_error());
        mysql_select_db("student_mchuang") or die(mysql_error());
    }
    
    function Create() {
        mysql_connect("sfsuswe.com", "mchuang", "GQaUAwTu59bUV2es") or die(mysql_error());
        mysql_select_db("student_mchuang") or die(mysql_error());
        
    }
    
    function Update() {
        mysql_connect("sfsuswe.com", "mchuang", "GQaUAwTu59bUV2es") or die(mysql_error());
        mysql_select_db("student_mchuang") or die(mysql_error());
        
    }
    
    
    
    function Delete() { 
        mysql_connect("sfsuswe.com", "mchuang", "GQaUAwTu59bUV2es") or die(mysql_error());
        mysql_select_db("student_mchuang") or die(mysql_error());
        $query = mysql_query("DELETE "
                                . "FROM Images "
                                . "WHERE id == '$id' "
                                ) or die("Error");
    } 
    
    
    
    
} 


?>
