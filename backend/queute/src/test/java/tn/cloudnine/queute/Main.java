package tn.cloudnine.queute;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println(new File("uploads/images/2a4b9ad5-6c18-4a27-89c7-e43961c09ee5_Fors_Wall_Official.jpg").getAbsolutePath());
        System.out.println(new File("uploads/images/2a4b9ad5-6c18-4a27-89c7-e43961c09ee5_Fors_Wall_Official.jpg").exists()); // should print true

    }
}
