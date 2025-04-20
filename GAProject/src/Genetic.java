import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genetic {
    private static final int POPULATION_SIZE = 5;
    private static final int GENERATIONS = 10000;
    private static final double MUTATION_RATE = 0.30;

    // Define CSP data
    private static final String[] CSPS = {"CSP_1", "CSP_2", "CSP_3", "CSP_4", "CSP_5", "CSP_6", "CSP_7", "CSP_8"};
    private static final double[][] CSP_INFORMATION = {
            {4.6, 0.8, 0.6},
            {5, 0.85, 0.4},
            {6, 0.9, 0.5},
            {4.8, 0.82, 0.6},
            {6.2, 0.92, 0.4},
            {6.5, 0.94, 0.5},
            {5.5, 0.88, 0.6},
            {7, 0.98, 0.4}
    };

    // Initializes the population of chromosomes
    // Each chromosome is in binary digits format
    private static List<String> initializePopulation() {
        List<String> populationList = new ArrayList<>();
        Random random = new Random();
        // Loop for the number of chromosomes defined by POPULATION_SIZE
        for (int i = 0; i < POPULATION_SIZE; i++) {
            StringBuilder chromosomeObj = new StringBuilder();
            // Loop for the number of CSPs defined by the length of CSPS
            for (int j = 0; j < CSPS.length; j++) {
                // Add a random binary digit (0 or 1) to the chromosome
                chromosomeObj.append(random.nextInt(2));
            }
            populationList.add(chromosomeObj.toString());
        }
        return populationList;
    }

    // Selection operation
    // Uses the tournament selection method to select the best chromosomes from the population.
    private static List<String> selectChromosomes(List<String> population) {
        List<String> selected = new ArrayList<>();
        while (selected.size() < POPULATION_SIZE / 2) {
            selected.add(tournamentSelection(population));
        }
        return selected;
    }

    // Tournament selection method.
    // It selects a group of chromosomes randomly from the population and returns the best one.
    private static String tournamentSelection(List<String> population) {
        Random randNum = new Random();
        int Size = 3;
        List<String> tournament = new ArrayList<>();
        // Select random chromosomes for the tournament selection
        for (int i = 0; i < Size; i++) {
            tournament.add(population.get(randNum.nextInt(population.size())));
        }
        // Sort the tournament by fitness in descending order
        tournament.sort((a, b) -> Double.compare(fitnessCalculation(b), fitnessCalculation(a)));
        // Return the chromosome with the highest fitness
        return tournament.get(0);
    }

    // Calculates the fitness
    private static double fitnessCalculation(String chromosome) {
        double cost = 0, reliability = 0, latency = 0;
        double maxCost = 0;
        double[] latencyIncrement = new double[CSPS.length]; // Add a latency increment for each CSP
        for (int i = 0; i < chromosome.length(); i++) {
            if (chromosome.charAt(i) == '1') {
                // Update maxCost if current cost is greater
                if (CSP_INFORMATION[i][0] > maxCost) {
                    maxCost = CSP_INFORMATION[i][0];
                }
            }
        }
        for (int i = 0; i < chromosome.length(); i++) {
            if (chromosome.charAt(i) == '1') {
                cost = 0.4 * (CSP_INFORMATION[i][0] / maxCost);
                reliability = 0.3 * (1 - CSP_INFORMATION[i][1]);
                latency = 0.3 * (CSP_INFORMATION[i][2] + latencyIncrement[i]); // Add the latency increment to the latency
                latencyIncrement[i] += 0.1; // Increase the latency increment by 0.1
            }
        }
        return cost + reliability + latency;
    }


    // Mutation operation
    private static void mutation(List<String> offspring) {
        Random random = new Random();
        // Loop through each chromosome in the offspring
        for (int i = 0; i < offspring.size(); i++) {
            String chromosome = offspring.get(i);
            StringBuilder newChromosome = new StringBuilder();
            // Loop through each gene in the chromosome
            for (int j = 0; j < chromosome.length(); j++) {
                // With a probability of MUTATION_RATE, flip the gene
                if (random.nextDouble() < MUTATION_RATE) {
                    newChromosome.append(chromosome.charAt(j) == '0' ? '1' : '0');
                } else {
                    newChromosome.append(chromosome.charAt(j));
                }
            }
            // Replace the old chromosome with the new one in the offspring list
            offspring.set(i, newChromosome.toString());
        }
    }

    // Generates the next generation of chromosomes.
    // The selected is a list of chromosomes selected from the current population.
    // The offspring is a list of chromosomes generated from the crossover operation.
    private static List<String> nextGeneration(List<String> selected, List<String> offspring) {
        // Create a new list for the next generation
        List<String> nextGeneration = new ArrayList<>();
        // Add the selected chromosomes and the offspring to the next generation
        nextGeneration.addAll(selected);
        nextGeneration.addAll(offspring);
        // Sort the next generation by fitness in descending order
        nextGeneration.sort((a, b) -> Double.compare(fitnessCalculation(b), fitnessCalculation(a)));
        // Only keep the top 50% chromosomes based on fitness
        nextGeneration = nextGeneration.subList(0, (int)(POPULATION_SIZE * 0.5));
        // Use tournament selection for the rest of the population
        while (nextGeneration.size() < POPULATION_SIZE) {
            nextGeneration.add(tournamentSelection(offspring));
        }
        return nextGeneration;
    }



    // single-point crossover method
    private static List<String> crossover(List<String> selected) {
        Random random = new Random();
        List<String> offspring = new ArrayList<>();
        for (int i = 0; i < selected.size() - 1; i += 2) {
            String parent1 = selected.get(i);
            String parent2 = selected.get(i + 1);
            // generate a random crossover point
            int crossoverPoint = random.nextInt(parent1.length() - 1) + 1;
            // make two children by swapping the tails of the parents
            String child1 = parent1.substring(0, crossoverPoint) + parent2.substring(crossoverPoint);
            String child2 = parent2.substring(0, crossoverPoint) + parent1.substring(crossoverPoint);
            // add children to offspring list
            offspring.add(child1);
            offspring.add(child2);
        }
        // if the number of selected chromosomes is odd, add the last chromosome to the offspring
        if (selected.size() % 2 != 0) {
            offspring.add(selected.get(selected.size() - 1));
        }
        return offspring;
    }

    public static void main(String[] args) {
        List<String> population = initializePopulation();

        for (int generation = 0; generation < GENERATIONS; generation++) {
            List<String> selected = selectChromosomes(population);
            List<String> offspring = crossover(selected);
            mutation(offspring);
            population = nextGeneration(selected, offspring);

            // Find the best solution in current generation
            String bestSolution = population.get(0);
            double bestFitness = fitnessCalculation(bestSolution);
            System.out.printf("Generation %d: Best solution - %s, Fitness - %.4f%n", generation + 1, bestSolution, bestFitness);
        }
    }
}



