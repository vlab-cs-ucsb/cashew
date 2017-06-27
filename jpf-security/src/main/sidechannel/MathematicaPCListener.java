package sidechannel;

/**
 * Created by bang on 11/22/16.
 */

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.vm.SystemState;
import sidechannel.ConcreteSideChannelListener;
import sidechannel.cost.approximate.SingleRunCost;
import sidechannel.util.MathematicaTranslator;
import sidechannel.util.MathematicaUtils;
//import sidechannel.util.ModelCollector;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;

public class MathematicaPCListener extends ConcreteSideChannelListener<Long> {

    public MathematicaPCListener(Config config, JPF jpf){
        super(config,jpf);
        mode = SMT_LIB2_MODE;
        costModel = new SingleRunCost(sideChannel,null);
        outputDirectory = config.getProperty("temp.directory");
    }

    String outputDirectory = null;
    String mathFile = "mathFile.m";

    Vector<PathCondition> PathConditions  = new Vector<PathCondition>();

    HashSet<String> setOfSymVar = new HashSet<String>();
    HashSet<String> symbolicVariables = new HashSet<String>();
    public MathematicaTranslator mathTranslator = new MathematicaTranslator(symbolicVariables);
//    public ModelCollector modelcollector = new ModelCollector(setOfSymVar);

    @Override
    protected void printCost(Long cost) {
        System.out.println("Cost is " + cost);
    }

    @Override
    protected void processPC(PathCondition pc, SystemState ss ){
        pc.solve();
        System.out.println(pc);
        System.out.println("------------------------");
        PathConditions.add(pc);
        numOfPCs++;
    }

    protected void writeFile(String content, String filename){

        BufferedWriter bw = null;
        FileWriter fw = null;
        String destination = outputDirectory + "/" + filename;
//        System.out.println(destination);

        try{
            fw = new FileWriter(destination);
            bw = new BufferedWriter(fw);
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void searchFinished(Search search){

        System.out.println("\nSearch finished.\n\n");
        System.out.println("\nPath Conditions:");
        System.out.println("-----------------------------------------------------------\n");

        int i = 0;
        for(PathCondition pc : PathConditions){
            System.out.println("PC " + i++);
            System.out.println(mathTranslator.translate(pc) + "\n");
        }

//        System.out.println("\n" + mathTranslator.translate(PathConditions));

        writeFile(mathTranslator.translate(PathConditions), mathFile);

        System.out.println("\n-----------------------------------------------------------");

        System.out.println("\nMathematica file written:");
        System.out.println(outputDirectory + "/" + mathFile);

        System.out.println("-----------------------------------------------------------\n");


    }

}

//System.out.println("\n===========================================");


//        System.out.println("\n\n" + PathConditions + "\n\n");



//        System.out.println(obsrv);
//        System.out.println("\n\n---------\n");
//        MathematicaUtils mathUtils = new MathematicaUtils(conf);
//
//        mathUtils.generateMathematicaScript(obsrv);


//collector.collectVariables(pc);
//modelcollector.collectVariables(pc);

//mathTranslator.translate(pc);


//        String currentPC = format(pc);
//        Long currentCost = costModel.getCurrentCost(ss);
//        HashSet<String> data = obsrv.get(currentCost);



//
//        if (data == null) {
//            data = new HashSet<String>();
//            data.add(currentPC);
//            obsrv.put(currentCost, data);
//            // reset current cost
//        }else{
//            data.add(currentPC);
//        }
//System.out.println("\nPC Prefix Notation:");

//System.out.println(pc.header.prefix_notation());

//System.out.println(currentPC);

