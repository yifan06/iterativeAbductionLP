package abd.knowledge.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;

public class Generator {

	  public static final void main(String... aArgs) throws IOException{
		    log("Generating 100 random formulas knowledge base with 26 variables.");
		    
//		    int nb_var = 26;
//		    int nb_formulas = 100;
//		    int nb_length = 6;
		    int nb_var = 9;
		    int nb_formulas = 9;
		    int nb_length = 4;
		    
		    Writer writer = new FileWriter("/home/yifan/workspace_eclipse/iterativeTab/kbset/generator_5_5/generator9.txt");
		  
		    
		    Random randomGenerator = new Random();
		    for (int idx = 1; idx <= nb_formulas; ++idx){
		      int length = randomGenerator.nextInt(nb_length);
		      log("Generated length: " + length);
		      if(length > 2){
		    	  for(int i=0;i<length; i++){
		    		  int var = randomGenerator.nextInt(nb_var);
		    		  boolean sign= randomGenerator.nextBoolean(); 
		    		  boolean connector;
	    			  if(sign)
	    				  writer.write("p"+var + " " );
	    			  else
	    				  writer.write("!p"+var +" ");
		    		  if(i!=length-1){
		    			  connector =  randomGenerator.nextBoolean(); 
//		    			  if(connector)
//		    				  writer.write("&& ");
//		    			  else
//		    				  writer.write("|| ");
		    			  writer.write("|| ");
		    		  }
		    			  
		    	  }
		    	  writer.write("\n");
		      }
		      else
		    	  idx--;
		    }
		    writer.close();
		  }
		  
		  private static void log(String aMessage){
		    System.out.println(aMessage);
		  }
}
