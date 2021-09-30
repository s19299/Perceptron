import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Perceptron {

    static List<Double> weights;
    static List<String> trainingData;
    static List<String> testData;
    static List<Flower> flowers;
    static List<Flower> flowersTest;
    static double alpha;
    static double theta = 0;

    public static void main(String[] args){

        System.out.println("Please enter value (between 0 and 1) of Alpha learning patamater and accept with ENTER:");
        perceptron();
        Scanner userIn = new Scanner(System.in);


        while(true){
            System.out.println("Please enter 0 if You wish to change the Alpha learning patameter and then press ENTER: ");
            if (userIn.nextLine().equals("0"))
                perceptron();
            else
                System.exit(0);
        }

    }


    public static void perceptron() {


        weights = new ArrayList<>();

        String lineOfInfo = null;
        String name = null;
        String nameTraining= null;



        /// W' = W + (d − y)αX

        Scanner in;
        in = new Scanner(System.in);
        alpha = in.nextDouble();
        System.out.println("Your aplha: " + alpha);

        try {

            Scanner test = new Scanner(new FileReader(new File("iris_test.txt")));
            Scanner training = new Scanner(new FileReader(new File("iris_training.txt")));


            trainingData = new ArrayList<>();
            while (training.hasNextLine()) {
                lineOfInfo = training.nextLine();
                if (lineOfInfo.contains(",")) {
                    lineOfInfo = lineOfInfo.replace(",", ".").trim();
                }
                trainingData.add(lineOfInfo);
            }

            int size = lineOfInfo.split("\t").length - 1;

            for (int i = 0; i < size; i++){
                weights.add(Math.random());
            }

            training.close();

            testData = new ArrayList<>();

            while (test.hasNextLine()) {
                lineOfInfo = test.nextLine();
                if (lineOfInfo.contains(",")) {
                    lineOfInfo = lineOfInfo.replace(",", ".").trim();
                }
                testData.add(lineOfInfo);

            }

            test.close();

            double classifications = 0;
            double correct = 0;

// zczytuje atrybuty do kwiatka (dziala odpowiednio sprawdzalam ktore wartosci gdzie ida) i dodaje do listy kwiatkow z treningu



            flowers = new ArrayList<>();

                for (String trainingLine : trainingData) {

                    String[] trainSplitted = trainingLine.split("\t");
                    double[] attributes = new double[trainSplitted.length - 1];

                    for (int i = 0; i < attributes.length; i++){

                        attributes[i] = Double.parseDouble(trainSplitted[i]);
                    }

                    nameTraining = trainSplitted[trainSplitted.length - 1].trim();

                    flowers.add(new Flower(nameTraining, attributes, alpha));

                }

                // dopoki dla isActive nie jest prawda dla wszystkich kwiatkow to petla leci i sprawdza wagi kwiatkow i ew modyfikuje

                boolean iterate = true;
                while (iterate) {

                    iterate = false;

                    for (Flower flower : flowers) {

                        if (!isActive(flower) && flower.getName().equals("Iris-setosa")) {
                            iterate = true;
                            theta = (1 - 0) * alpha;
                            for (int i = 0; i < weights.size(); i++){
                                double newWeigh = weights.get(i) + ((1 - 0)*alpha*flower.getAttributesList().get(i));
                                weights.set(i, newWeigh);
                            }
                        }
                        else if (isActive(flower) && !flower.getName().equals("Iris-setosa")) {
                            iterate = true;
                            theta = (0 - 1) * alpha;
                            for (int i = 0; i < weights.size(); i++){
                                double newWeigh = weights.get(i) + ((0 - 1)*alpha*flower.getAttributesList().get(i));
                                weights.set(i, newWeigh);
                            }
                        }
                        else if(isActive(flower) && flower.getName().equals("Iris-setosa"))
                            iterate = false;
                    }
                }

                flowersTest = new ArrayList<>();


                for (String testLine : testData) {

                String[] splittedTest = testLine.split("\t");
                name = splittedTest[splittedTest.length - 1].trim();
                double[] attributes = new double[splittedTest.length - 1];

                for (int i = 0; i < splittedTest.length-1; i++){
                    attributes[i] = Double.parseDouble(splittedTest[i]);
                }

                flowersTest.add(new Flower(name, attributes, alpha));
            }

            for (Flower testFlower : flowersTest) {

                if (isActive(testFlower) && testFlower.getName().equals("Iris-setosa") || !isActive(testFlower) && !testFlower.getName().equals("Iris-setosa")){
                    correct++;
                    System.out.println(testFlower.getName() + " zakwalifikowany jako Iris-setosa: " + isActive(testFlower));
                }

                classifications++;

            }

            System.out.println("Procentowa skutecznosc algorytmu: "  + correct/classifications*100 + "%");
            for (double weight: weights) {
                System.out.println("waga: " + weight);
            }
           // }
            System.out.println("Please enter in format (double, double, double , double) own attributes for a flower then press ENTER: ");
            Scanner scanner = new Scanner(System.in);
            String[] vector = scanner.nextLine().split(",");
            double[] attri = new double[vector.length];

            for (int i = 0; i < attri.length; i++){
                attri[i] = Double.parseDouble(vector[i]);
            }

            Flower flower = new Flower(null, attri, alpha);

            if (isActive(flower)){
                System.out.println("Iris-setosa");
            }
            else
                System.out.println("inny kwiat");


        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


// sprawdza wartosc y czy 0 czy 1

    static boolean isActive(Flower flower){


            double d = flower.sumOutput(weights);

            //theta to 0
            if (d >= theta){
                return true;
            }

            else
                return false;

    }

}

// kwiatek jako obiekt

class Flower {


    public String name;
    // attributes to wektory wejsciowe
    public List<Double> attributes = new ArrayList<>();
    double alpha;


    Flower(String name, double[] attributes, double alpha) {
        this.name = name;
        this.setAttributes(attributes);
        this.alpha = alpha;

    }


    String getName() {
        return this.name;
    }


    List<Double> getAttributesList() {
        return this.attributes;
    }

    // ustala liste atrybutow numerycznych

    void setAttributes(double[] attributes) {

        for (int i = 0; i < attributes.length; i++) {
            this.attributes.add(attributes[i]);
        }
    }

    // sumuje z wag wartosc

    int sumOutput(List<Double> weights) {

        int sum = 0;

        for (int i = 0; i < this.attributes.size(); i++) {
            sum += weights.get(i) * this.attributes.get(i);
        }

        return sum;
    }
}

