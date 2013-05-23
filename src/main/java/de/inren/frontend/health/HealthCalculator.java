/**
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.inren.frontend.health;

/**
 * @author Ingo Renner
 *
 */
public class HealthCalculator {
    
    /** Correction factors for the body mass if some parts of the body are missing. */
    public static enum Corrections {
        Hand(0.008),
        Unterarm(0.023),
        Oberarm(0.035),
        Fuss(0.018),
        Unterschenkel(0.053),
        Oberschenkel(0.116);
        
        private final double corection;
        
        Corrections(double corection) {
            this.corection = corection;
        }

        public double getCorection() {
            return corection;
        }
    }
    
    
    /** Calculate BMI */
    public static double calculateBmi(double size, double mass) {
        return mass / (size * size);
    }
    
    /** Calculate body mass if some parts of the body are missing */
    public static double calculateBmiMass(double mass, double ... corrections) {
        double sum = 0.0;
        for (double c : corrections) {
            sum += c;
        }
        return mass *  (1.0 / (1.0 - sum));
    }

    public static enum BmiMeaning {
        Very_Severely_Underweight(0.0, 15.0, 0.0, 0.60, "#2A50FF"),
        Severely_Underweight(15.0, 16.0, 0.60, 0.64, "#3A60BB"),
        Underweight(16.0, 18.5, 0.64, 0.74, "#4A708B"),
        Normal(18.5, 2.05, 0.74, 1.0, "#006400"),
        Overweight(25.0, 30.0, 1.0, 1.2, "#FFFF00"),
        Obese_Class_I(30.0, 35.0, 1.2, 1.4, "#FFA500"),
        Obese_Class_II(35.0, 40.0,1.4, 1.6, "#FF0000"),
        Obese_lass_III(40.0, null, 1.6, null, "#8B0000");
        
        private final Double bMin;
        private final Double bMax;
        private final Double bpMin;
        private final Double bpMax;
        private String color;

        private BmiMeaning(Double bMin, Double bMax, Double bpMin, Double bpMax, String color) {
            this.bMin = bMin;
            this.bMax = bMax;
            this.bpMin = bpMin;
            this.bpMax = bpMax;
            this.color = color;
        }

        public Double getbMin() {
            return bMin;
        }

        public Double getbMax() {
            return bMax;
        }

        public Double getBpMin() {
            return bpMin;
        }

        public Double getBpMax() {
            return bpMax;
        }
        
        public String getColor() {
            return color;
        }
    }
}
