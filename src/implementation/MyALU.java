/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import utilitytypes.EnumComparison;
import utilitytypes.EnumOpcode;

/**
 * The code that implements the ALU has been separates out into a static
 * method in its own class.  However, this is just a design choice, and you
 * are not required to do this.
 * 
 * @author 
 */
public class MyALU {
    static int execute(EnumOpcode opcode, int input1, int input2, int oper0) {
        int result = 0;
        //GlobalData globals = (GlobalData)core.getGlobalResources();
       // int[] regfile = globals.register_file;
        
        
        if(EnumOpcode.ADD.equals(opcode))
        {
        	oper0=input1+input2;
        	System.out.println("value1: "+input1+"value2: "+input2);
        	System.out.println("The result of addition:"+oper0);
        	result=oper0;
        	
        }
        if(EnumOpcode.SUB.equals(opcode))
        {
        	oper0=input1-input2;
        	System.out.println("value1: "+input1+"value2: "+input2);
        	System.out.println("The result of sub:"+oper0);
           	
        	result=Math.abs(oper0);        	
        }
        if(EnumOpcode.SHL.equals(opcode))
        {
        	oper0=input1 <<= input2;
        	result=oper0;
        }
        if(EnumOpcode.ASR.equals(opcode))
        {
        	oper0=input1 >> input2;
        	result=oper0;
        }
        if(EnumOpcode.LSR.equals(opcode))
        {
        	oper0=input1 >>>= input2;
        	result=oper0;
        }
        if(EnumOpcode.AND.equals(opcode))
        {
        	oper0=oper0 & input1;
        	result=oper0;
        }
        if(EnumOpcode.OR.equals(opcode))
        {
        	oper0=oper0 | input1;
        	result=oper0;
        }
        if(EnumOpcode.XOR.equals(opcode))
        {
        	oper0=oper0 ^ input1;
        	result=oper0;
        }
        if(EnumOpcode.MULS.equals(opcode))
        {
        	oper0=input1*input2;
        	System.out.println("value1 "+input1+"value2"+input2);
        	System.out.println("The result of MULTIPLICATION:"+oper0);
           	
        	result=oper0;
        }
        if(EnumOpcode.STORE.equals(opcode))
        {
        	int address=input1 + input2;
        	System.out.println("The result of store1:"+address);
        	result=address;
        }
        if(EnumOpcode.LOAD.equals(opcode))
        {
        	int address=input1 + input2;
        	System.out.println("The result of LOAD:"+address);
        	result=address;
        }
        
        // Implement code here that performs appropriate computations for
        // any instruction that requires an ALU operation.  See
        // EnumOpcode.*/
        
        return result;
    }
    
    /*static boolean execute(EnumComparison opcode, int input1, int input2, int oper0) 
    {
    	boolean result = false;
    	boolean outcome;
        if(EnumComparison.EQ.equals(opcode))
        {
        	outcome = input1==input2;
            if(outcome)
        	result=outcome;
        }
        if(EnumComparison.NE.equals(opcode))
        {
        	outcome=input1-input2!=0;
        	if(outcome)
        	result= outcome;
        }
        if(EnumComparison.GT.equals(opcode))
        {
        	outcome=input1>input2;
        	if(outcome)
        	result=outcome;
        }
        if(EnumComparison.GE.equals(opcode))
        {
        	outcome=input1>=input2;
        	if(outcome)
        	result=outcome;
        }
        if(EnumComparison.GT.equals(opcode))
        {
        	outcome=input1-input2==0;
        	if(outcome)
        	result=outcome;
        }
        if(EnumComparison.LT.equals(opcode))
        {
        	outcome=input1<input2;
        	if(outcome)
        	result=outcome;
        }
        if(EnumComparison.LE.equals(opcode))
        {
        	outcome=input1<=input2;
        	if(outcome)
        	result=outcome;
        }
        return result;
}*/
}
