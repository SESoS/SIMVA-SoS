package new_simvasos.verifier;

import javafx.util.Pair;
import new_simvasos.log.Log;
import new_simvasos.property.Property;
import new_simvasos.property.PropertyChecker;
import new_simvasos.simulation.Simulation;
import new_simvasos.simulator.Simulator;

public class SPRT extends Verifier {
    double alpha;
    double beta;
    double delta;
    int minimumSample;
    
    public SPRT(PropertyChecker checker) {
        super(checker);
        this.alpha = 0.05;
        this.beta = 0.05;
        this.delta = 0.01;
        this.minimumSample = 2;
    }
    
    // Return <pair for drawing graph<number of samples, true/false>,
    // verification result on each theta>
    // Existence, Absence, Universality Check
    public Pair<Pair<Integer,Boolean>, String> verifyWithSimulationGUI(Simulation simulation, Property verificationProperty, int maxRepeat, double theta) {
        int maxNumSamples = maxRepeat;
        boolean ret = true;
        int numSamples;
        int numTrue;
        
        numSamples = 0;
        numTrue = 0;
        
        while (this.isSampleNeeded(numSamples, numTrue, theta)) {
            if (!(numSamples < maxNumSamples)) {
                System.out.println("Over maximum repeat: " + maxNumSamples);
                break;
            }
            
            Log log = simulation.runSimulation();
            
            if (this.propertychecker.check(log, verificationProperty)) {
                
                numTrue += 1;
            }
            numSamples += 1;
        }
        
        ret = this.isSatisfied(numSamples, numTrue, theta);
        String verificationResult = "theta: " + Double.parseDouble(String.format("%.2f", theta)) +
            " numSamples: " + numSamples + " numTrue: " + numTrue + " result: " + ret;
        
        
        return new Pair(new Pair(numSamples, ret), verificationResult);
    }
    // Minimum Duration Check
    public Pair<Pair<Integer,Boolean>, String> verifyWithSimulationGUI(Simulation simulation, Property verificationProperty, int maxRepeat, double theta, int t, int T) {
        int maxNumSamples = maxRepeat;
        boolean ret = true;
        int numSamples;
        int numTrue;
        
        numSamples = 0;
        numTrue = 0;
        
        while (this.isSampleNeeded(numSamples, numTrue, theta)) {
            if (!(numSamples < maxNumSamples)) {
                System.out.println("Over maximum repeat: " + maxNumSamples);
                break;
            }
            
            Log log = simulation.runSimulation();
            
            //MinimumDurationChecker
            if(this.propertychecker.check(log, verificationProperty, t, T)) {
                numTrue += 1;
            }
            numSamples += 1;
        }
        
        ret = this.isSatisfied(numSamples, numTrue, theta);
        String verificationResult = "theta: " + Double.parseDouble(String.format("%.2f", theta)) +
            " numSamples: " + numSamples + " numTrue: " + numTrue + " result: " + ret;
        
        
        return new Pair(new Pair(numSamples, ret), verificationResult);
    }
    
    // SteadyStateProbability Check
    public Pair<Pair<Integer,Boolean>, String> verifyWithSimulationGUI(Simulation simulation, Property verificationProperty, int maxRepeat, double theta, double p, int T) {
        int maxNumSamples = maxRepeat;
        boolean ret = true;
        int numSamples;
        int numTrue;
        
        numSamples = 0;
        numTrue = 0;
        
        while (this.isSampleNeeded(numSamples, numTrue, theta)) {
            if (!(numSamples < maxNumSamples)) {
                System.out.println("Over maximum repeat: " + maxNumSamples);
                break;
            }
            
            Log log = simulation.runSimulation();
            
            if(this.propertychecker.check(log, verificationProperty, p, T)) {
                numTrue += 1;
            }
            numSamples += 1;
        }
        
        ret = this.isSatisfied(numSamples, numTrue, theta);
        String verificationResult = "theta: " + Double.parseDouble(String.format("%.2f", theta)) +
            " numSamples: " + numSamples + " numTrue: " + numTrue + " result: " + ret;
        
        
        return new Pair(new Pair(numSamples, ret), verificationResult);
    }
    
    // Transient State Probability Check
    public Pair<Pair<Integer,Boolean>, String> verifyWithSimulationGUI(Simulation simulation, Property verificationProperty, int maxRepeat, double theta, double p, int t, int T) {
        int maxNumSamples = maxRepeat;
        boolean ret = true;
        int numSamples;
        int numTrue;
        
        numSamples = 0;
        numTrue = 0;
        
        while (this.isSampleNeeded(numSamples, numTrue, theta)) {
            if (!(numSamples < maxNumSamples)) {
                System.out.println("Over maximum repeat: " + maxNumSamples);
                break;
            }
            
            Log log = simulation.runSimulation();
            
            if(this.propertychecker.check(log, verificationProperty, p, t, T)) {
                numTrue += 1;
            }
            numSamples += 1;
        }
        
        ret = this.isSatisfied(numSamples, numTrue, theta);
        String verificationResult = "theta: " + Double.parseDouble(String.format("%.2f", theta)) +
            " numSamples: " + numSamples + " numTrue: " + numTrue + " result: " + ret;
        
        
        return new Pair(new Pair(numSamples, ret), verificationResult);
    }
    
    private boolean isSampleNeeded(int numSample, int numTrue, double theta) {
        if (numSample < this.minimumSample) return true;
        
        double h0Threshold = this.beta / (1-this.alpha);
        double h1Threshold = (1-this.beta) / this.alpha;
        
        double v = this.getV(numSample, numTrue, theta);
        
        if (v <= h0Threshold) {
            return false;
        }
        else if (v >= h1Threshold) {
            return false;
        }
        else {
            return true;
        }
    }
    
    private boolean isSatisfied(int numSamples, int numTrue, double theta) {
        double h0Threshold = this.beta/(1-this.alpha);
        
        double v = this.getV(numSamples, numTrue, theta);
        
        if (v <= h0Threshold) {
            return true;
        } else  {
            return false;
        }
    }
    
    private double getV(int numSample, int numTrue, double theta) {
        double p0 = theta + this.delta;
        double p1 = theta - this.delta;
        
        int numFalse = numSample - numTrue;
        
        double p1m = Math.pow(p1, numTrue) * Math.pow((1-p1), numFalse);
        double p0m = Math.pow(p0, numTrue) * Math.pow((1-p0), numFalse);
        
        if (p0m == 0) {
            p1m = p1m + Double.MIN_VALUE;
            p0m = p0m + Double.MIN_VALUE;
        }
        
        return p1m / p0m;
    }
    
}

