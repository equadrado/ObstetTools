package com.equadrado.model;

/**
 * Created by equadrado on 2016-10-31.
 */

public class FetalWeight {
    private int PESO1_P1;
    private int PESO1_P5;
    private int PESO1_P9;

    private int weight1;
    private int weight2;
    private int weight3;

    public final String WEIGHT_REF_1 = "Deter RL, Harist RB, Hadlock FP et al 'The use of ultasound in the assesment of normal fetal growth'\n" +
                               "J Clin Ultrasound 9:481-83, 1981";
    public final String WEIGHT_REF_2 = "Hadlock FP, 'Estiamtions of fetal weight with the use of head, body and femur measurements. Aprospective study'\n" +
                               "Am J Obstet Gynecol 151:333, 1985";
    public final String WEIGHT_REF_3 = "Shepard MJ, 'An evaluation of two equations for predicting fetal weight by ultrassound',\n" +
                               "Am J Obstet Gynecol 142: 47, 1982.";

    public FetalWeight() {

    }

    public int getPESO1_P1() {
        return PESO1_P1;
    }

    public void setPESO1_P1(int PESO1_P1) {
        this.PESO1_P1 = PESO1_P1;
    }

    public int getPESO1_P5() {
        return PESO1_P5;
    }

    public void setPESO1_P5(int PESO1_P5) {
        this.PESO1_P5 = PESO1_P5;
    }

    public int getPESO1_P9() {
        return PESO1_P9;
    }

    public void setPESO1_P9(int PESO1_P9) {
        this.PESO1_P9 = PESO1_P9;
    }

/*
         nFormula := (-1.7492 + (0.166*nDBP) + (0.046*nCA) - ((2.646*nCA*nDBP)/1000));
                     // Shepard MJ, 'An evaluation of two equations for predicting fetal weight by ultrassound',
                     // Am J Obstet Gynecol 142: 47, 1982.

         nFormula := (1.3596 - (0.00386*nCA*nFem) + (0.0064*nCC) + (0.00061*nDBP*nCA) + (0.0424*nCA) + (0.174*nFem));
                     // Hadlock FP, 'Estiamtions of fetal weight with the use of head, body and femur measurements. Aprospective study'
                     // Am J Obstet Gynecol 151:333, 1985

*         nFormula := (1.326 - (0.0000326*nCA*nFem) + (0.00107*nCC) + (0.00438*nCA) + (0.0158*nFem));
                     // Deter RL, Harist RB, Hadlock FP et al 'The use of ultasound in the assesment of normal fetal growth'
                     // J Clin Ultrasound 9:481-83, 1981

         nFormula := (1.5115 + (0.0436*nCA) + (0.1517*nFem) - ((0.321*nCA*nFem)/100) + ((0.6923*nDBP*nCC)/10000));
                     // Hadlock FP, 'Sonographic estimations of fetal weight' Radiology, 150:535, 1984
 */

    public int getWeight1() {
        return weight1;
    }

    public void setWeight1(int medCC, int medCA, int medFEM) {
        this.weight1 = 0;
        if (medCC > 0 && medCA > 0 && medFEM > 0) {
            double nFormula = (1.326 - (0.0000326*medCA*medFEM) + (0.00107*medCC) + (0.00438*medCA) + (0.0158*medFEM));
            if (nFormula > 0) {
                this.weight1 = (int) Math.round(Math.pow(10, nFormula));
            }
        }
    }

    public int getWeight2() {
        return weight2;
    }

    public void setWeight2(int medCC, int medCA, int medFEM) {
        this.weight2 = 0;
        if (medCC > 0 && medCA > 0 && medFEM > 0) {
            double nFormula = (1.3598 + (0.0051 * medCA) + (0.01844 * medFEM) - (0.000037 * medCA * medFEM));
            if (nFormula > 0) {
                this.weight2 = (int) Math.round(Math.pow(10, nFormula));
            }
        }
    }

    public int getWeight3() {
        return weight3;
    }

    public void setWeight3(int medDBP, int medCA) {
        this.weight3 = 0;
        if (medDBP > 0 && medCA > 0) {
            double nFormula = (-1.7492 + (0.166*medDBP) + (0.046*medCA) - ((2.646*medCA*medDBP)/1000));
            if (nFormula > 0) {
                this.weight3 = (int) Math.round(Math.pow(10, nFormula));
            }
        }
    }

    public void calculateAll(int medCC, int medCA, int medFEM, int medDBP){
        setWeight1(medCC, medCA, medFEM);

        setWeight2(medCC, medCA, medFEM);

        setWeight3(medDBP, medCA);
    }
}
