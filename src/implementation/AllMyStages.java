/*
 * To changeperat this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import baseclasses.CpuCore;
import baseclasses.InstructionBase;
import baseclasses.PipelineRegister;
import baseclasses.PipelineStageBase;
import implementation.AllMyLatches.DecodeToExecute;
import implementation.AllMyLatches.ExecuteToMemory;
import implementation.AllMyLatches.FetchToDecode;
import implementation.AllMyLatches.MemoryToWriteback;
import utilitytypes.EnumOpcode;
import voidtypes.VoidLatch;

/**
 * The AllMyStages class merely collects together all of the pipeline stage
 * classes into one place. You are free to split them out into top-level
 * classes.
 * 
 * Each inner class here implements the logic for a pipeline stage.
 * 
 * It is recommended that the compute methods be idempotent. This means that if
 * compute is called multiple times in a clock cycle, it should compute the same
 * output for the same input.
 * 
 * How might we make updating the program counter idempotent?
 * 
 * @author
 */
public class AllMyStages {
	/*** Fetch Stage ***/

	//static int flag_check = 0;
	//static int flagCheckArray[] = new int[32];

	static class Fetch extends PipelineStageBase<VoidLatch, FetchToDecode> {
		public Fetch(CpuCore core, PipelineRegister input, PipelineRegister output) {
			super(core, input, output);
		}

		@Override
		public String getStatus() {
			// Generate a string that helps you debug.
			return null;
		}

		@Override
		public void compute(VoidLatch input, FetchToDecode output) {
			GlobalData globals = (GlobalData) core.getGlobalResources();
			int pc = globals.program_counter;
			InstructionBase ins = globals.program.getInstructionAt(pc);

			String inst[] = ins.toString().split(" ");
			// inst[2]=inst[2].replaceAll("R", "");

			//if (flag_check == 0) {
				globals.program_counter++;
				output.setInstruction(ins);
//			} else {
//				output.setInstruction(null);
//			}

			if (ins.isNull())
				return;
			System.out.println("ins:" + ins);

			// Do something idempotent to compute the next program counter.

			// Don't forget branches, which MUST be resolved in the Decode
			// stage. You will make use of global resources to commmunicate
			// between stages.

			// Your code goes here...
			// advanceClock();

		}

		@Override
		public boolean stageWaitingOnResource() {
			// Hint: You will need to implement this for when branches
			// are being resolved.
			return false;
		}

		/**
		 * This function is to advance state to the next clock cycle and can be applied
		 * to any data that must be updated but which is not stored in a pipeline
		 * register.
		 */
		@Override
		public void advanceClock() {
			GlobalData globals = (GlobalData) core.getGlobalResources();
			int pc = globals.program_counter;
			System.out.println("counter is: " + pc);

			// Hint: You will need to implement this help with waiting
			// for branch resolution and updating the program counter.
			// Don't forget to check for stall conditions, such as when
			// nextStageCanAcceptWork() returns false.
		}
	}

	/*** Decode Stage ***/
	static class Decode extends PipelineStageBase<FetchToDecode, DecodeToExecute> {
		public Decode(CpuCore core, PipelineRegister input, PipelineRegister output) {
			super(core, input, output);
		}

		@Override
		public boolean stageWaitingOnResource() {
			// Hint: You will need to implement this to deal with
			// dependencies.
			return false;
		}

		@Override
		public void compute(FetchToDecode input, DecodeToExecute output) {
			System.out.println("Hi this is decode");

			InstructionBase ins = input.getInstruction();
			System.out.println("Decode ins:" + ins);

			// These null instruction checks are mostly just to speed up
			// the simulation. The Void types were created so that null
			// checks can be almost completely avoided.
			if (ins.isNull())
				return;

			GlobalData globals = (GlobalData) core.getGlobalResources();
			int[] regfile = globals.register_file;

			EnumOpcode operation = ins.getOpcode();

			// Do what the decode stage does:
			// - Look up source operands
			// - Decode instruction
			// - Resolve branches

			// System.out.println(ins.toString()+" This is string ");
			
			String inst[] = ins.toString().split(" ");
			inst[0] = inst[0].replaceAll(":", "");

			int source1 = 0;
			int source2 = 0;
			int oper0 = 0;

			if (operation.name() == "ADD" || operation.name() == "SUB" || operation.name() == "MULS") {
				if (inst[2].contains("R")) {
					oper0 = Integer.parseInt(inst[2].replaceAll("R", ""));
					// globals.register_invalid[oper0]=true;
				}
				if (inst[3].contains("R")) {
					inst[3] = inst[3].replaceAll("R", "");
				}
				if (inst[4].contains("R")) {
					inst[4] = inst[4].replaceAll("R", "");
				}
//				if (globals.register_invalid[oper0] == false) {
//					globals.register_invalid[oper0] = true;
			//		flag_check = 1;

//					if (globals.register_invalid[Integer.parseInt(inst[3])] == false
//							&& globals.register_invalid[Integer.parseInt(inst[4])] == false) {
				//		flag_check = 0;
						source1 = regfile[Integer.parseInt(inst[3])];
						source2 = regfile[Integer.parseInt(inst[4])];

						output.source1 = source1;
						output.source2 = source2;
						output.oper0 = oper0;
						output.setInstruction(ins);
						// flag_check=0;
//				}

//				}
				// else flagCheckArray[oper0]=1;
			}
			if (operation.name() == "MOVC") {
				if (inst[2].contains("R")) {
					oper0 = Integer.parseInt(inst[2].replaceAll("R", ""));
					// globals.register_invalid[oper0]=true;
				}

//				if (globals.register_invalid[oper0] == false) {
//					globals.register_invalid[oper0] = true;
//					
					output.source1 = source1;
					output.oper0 = oper0;
					output.setInstruction(ins);
					
					//flag_check=0;
//				} 
			}

			if (operation.name() == "STORE") {
				if (inst[2].contains("R")) {
					inst[2] = inst[2].replaceAll("R", "");
				}
				if (inst[3].contains("R")) {
					inst[3] = inst[3].replaceAll("R", "");
				}
				if (inst[4].contains("R")) {
					inst[4] = inst[4].replaceAll("R", "");
				}
//				if (globals.register_invalid[Integer.parseInt(inst[2])] == false
//						&& globals.register_invalid[Integer.parseInt(inst[3])] == false
//						&& globals.register_invalid[Integer.parseInt(inst[4])] == false) {
					oper0 = regfile[Integer.parseInt(inst[2])];
					source1 = regfile[Integer.parseInt(inst[3])];
					source2 = regfile[Integer.parseInt(inst[4])];

					output.source1 = source1;
					output.source2 = source2;
					output.oper0 = oper0;
					output.setInstruction(ins);
					// flag_check=0;
//				}
				// else flag_check=1;

			}

			if (operation.name() == "AND") {
				// globals.register_invalid[oper0]=true;
				if (inst[2].contains("R")) {
					inst[2] = inst[2].replaceAll("R", "");
					oper0 = regfile[Integer.parseInt(inst[2])];

				}

				if (inst[3].contains("R")) {
					inst[3] = inst[3].replaceAll("R", "");
				}
//				if (globals.register_invalid[oper0] == false) {
//					globals.register_invalid[oper0] = true;
					//flag_check = 1;
//					if (globals.register_invalid[Integer.parseInt(inst[3])] == false) {
						source1 = regfile[Integer.parseInt(inst[3])];

						output.source1 = source1;
						output.oper0 = oper0;
						output.setInstruction(ins);
						//flag_check = 0;
//					}
					// else flag_check=1;

//				}
				// else
			}
			if (operation.name() == "OR") {
				// globals.register_invalid[oper0]=true;
				if (inst[2].contains("R")) {
					inst[2] = inst[2].replaceAll("R", "");
					oper0 = regfile[Integer.parseInt(inst[2])];
					// globals.register_invalid[oper0]=true;
				}
				// if(inst[3].contains("R"))
				// {
				// inst[3]=inst[3].replaceAll("R", "");
				// source1=regfile[Integer.parseInt(inst[3])];
				// }
				if (inst[3].contains("R")) {
					inst[3] = inst[3].replaceAll("R", "");
				}
//				if (globals.register_invalid[oper0] == false) {
//					globals.register_invalid[oper0] = true;
					//flag_check = 1;

//					if (globals.register_invalid[Integer.parseInt(inst[3])] == false) {
						source1 = regfile[Integer.parseInt(inst[3])];

						output.source1 = source1;
						output.oper0 = oper0;
						output.setInstruction(ins);
						//flag_check = 0;
//					}
					// else flag_check=1;

//				}
				// else flag_check=1;
			}
			if (operation.name() == "XOR") {
				// globals.register_invalid[oper0]=true;
				if (inst[2].contains("R")) {
					inst[2] = inst[2].replaceAll("R", "");
					oper0 = regfile[Integer.parseInt(inst[2])];
					// globals.register_invalid[oper0]=true;
				}
				if (inst[3].contains("R")) {
					inst[3] = inst[3].replaceAll("R", "");
				}
//				if (globals.register_invalid[oper0] == false) {
//					globals.register_invalid[oper0] = true;
					//flag_check = 1;

//					if (globals.register_invalid[Integer.parseInt(inst[3])] == false) {
						source1 = regfile[Integer.parseInt(inst[3])];

						output.source1 = source1;
						output.oper0 = oper0;
						output.setInstruction(ins);
						//flag_check = 0;

//					}
					// else flag_check=1;

//				}
				// else flag_check=1;
			}

			if (operation.name() == "LOAD") {
				// globals.register_invalid[oper0]=true;
				if (inst[2].contains("R")) {
					oper0 = Integer.parseInt(inst[2].replaceAll("R", ""));
					// globals.register_invalid[oper0]=true;
				}

				if (inst[3].contains("R")) {
					inst[3] = inst[3].replaceAll("R", "");
				}
				if (inst[4].contains("R")) {
					inst[4] = inst[4].replaceAll("R", "");
				}

//				if (globals.register_invalid[oper0] == false) {
//					globals.register_invalid[oper0] = true;
					//flag_check = 1;

//					if (globals.register_invalid[Integer.parseInt(inst[3])] == false
//							&& globals.register_invalid[Integer.parseInt(inst[4])] == false) {
						source1 = regfile[Integer.parseInt(inst[3])];
						source2 = regfile[Integer.parseInt(inst[4])];

						output.source1 = source1;
						output.source2 = source2;
						output.oper0 = oper0;
						output.setInstruction(ins);
						//flag_check = 0;
//					}
					// else flag_check=1;
//				}
				// else flag_check=1;
			}

			// output.source1=source1;
			// output.source2=source2;
			// output.oper0=oper0;
			// output.setInstruction(ins);
			// Set other data that's passed to the next stage.
		}
	}

	/*** Execute Stage ***/
	static class Execute extends PipelineStageBase<DecodeToExecute, ExecuteToMemory> {
		public Execute(CpuCore core, PipelineRegister input, PipelineRegister output) {
			super(core, input, output);
		}

		@Override
		public void compute(DecodeToExecute input, ExecuteToMemory output) {
			InstructionBase ins = input.getInstruction();

			if (ins.isNull())
				return;

			// int source1 = ins.getSrc1().getValue();
			// int source2 = ins.getSrc2().getValue(); //PROF ka hai ye...
			// int oper0 = ins.getOper0().getValue();

			GlobalData globals = (GlobalData) core.getGlobalResources();
			int[] regfile = globals.register_file;

			EnumOpcode operation = ins.getOpcode();

			// System.out.println("Excecute ins:"+ins);
			// System.out.println("Sourse 1 ins:"+source1);
			// System.out.println("Sourse 2 ins:"+source2);
			// System.out.println("oper0 ins:"+oper0);

			System.out.println("Execute hai ye: " + ins);

			int result = 0;
			/*
			 * String inst[]= ins.toString().split(" ");
			 * 
			 * 
			 * if(operation.name()=="ADD"||operation.name()=="SUB"||operation.name()==
			 * "MULS") { if(inst[3].contains("R")) { inst[3]=inst[3].replaceAll("R", "");
			 * source1=regfile[Integer.parseInt(inst[3])]; } if(inst[4].contains("R")) {
			 * inst[4]=inst[4].replaceAll("R", "");
			 * source2=regfile[Integer.parseInt(inst[4])]; } } if(operation.name()=="STORE")
			 * { if(inst[2].contains("R")) { inst[2]=inst[2].replaceAll("R", "");
			 * oper0=regfile[Integer.parseInt(inst[2])]; } if(inst[3].contains("R")) {
			 * inst[3]=inst[3].replaceAll("R", "");
			 * source1=regfile[Integer.parseInt(inst[3])]; } if(inst[4].contains("R")) {
			 * inst[4]=inst[4].replaceAll("R", "");
			 * source2=regfile[Integer.parseInt(inst[4])]; }
			 * 
			 * } if(operation.name()=="LOAD") { if(inst[3].contains("R")) {
			 * inst[3]=inst[3].replaceAll("R", "");
			 * source1=regfile[Integer.parseInt(inst[3])]; } if(inst[4].contains("R")) {
			 * inst[4]=inst[4].replaceAll("R", "");
			 * source2=regfile[Integer.parseInt(inst[4])]; } }
			 */

			int source1 = input.source1;
			int source2 = input.source2;
			int oper0 = input.oper0;

			if (operation.name() == "ADD" || operation.name() == "MOVC" || operation.name() == "STORE"
					|| operation.name() == "SUB" || operation.name() == "SHL" || operation.name() == "ASR"
					|| operation.name() == "LSR" || operation.name() == "AND" || operation.name() == "OR"
					|| operation.name() == "XOR" || operation.name() == "MULS" || operation.name() == "LOAD")

			{
				// System.out.println("src1: "+source1+" src2 "+source2);
				result = MyALU.execute(ins.getOpcode(), source1, source2, oper0);

				// GlobalData globals = (GlobalData)core.getGlobalResources();
				// int[] regfile = globals.register_file;

				// regfile[ins.getOper0().getRegisterNumber()]=result;
				// System.out.println("Addition: "+regfile[ins.getOper0().getRegisterNumber()]);

			}

			// store add karo

			// ins.setSrc1(Operand(source1));
			// System.out.println(output+" OUTPUT HAI YE");

			// Fill output with what passes to Memory stage...
			// output.setInvalid();
			output.finalResult = result;
			output.setInstruction(ins);

			// System.out.println(output.finalResult);

			// Set other data that's passed to the next stage.
		}
	}

	/*** Memory Stage ***/
	static class Memory extends PipelineStageBase<ExecuteToMemory, MemoryToWriteback> {
		public Memory(CpuCore core, PipelineRegister input, PipelineRegister output) {
			super(core, input, output);
		}

		@Override
		public void compute(ExecuteToMemory input, MemoryToWriteback output) {
			InstructionBase ins = input.getInstruction();
			GlobalData globals = (GlobalData) core.getGlobalResources();
			int[] regfile = globals.register_file;

			String inst[] = ins.toString().split(" ");
			System.out.println("Memory ins:" + ins);
			EnumOpcode operation = ins.getOpcode();

			if (operation.name() == "ADD") {
				output.finalResult = input.finalResult;
			}
			if (operation.name() == "SUB") {
				output.finalResult = input.finalResult;
			}
			if (operation.name() == "MOVC") {
				output.finalResult = input.finalResult;
			}
			if (operation.name() == "MULS") {
				output.finalResult = input.finalResult;
			}
			if (operation.name() == "AND") {
				output.finalResult = input.finalResult;
			}
			if (operation.name() == "OR") {
				output.finalResult = input.finalResult;
			}
			if (operation.name() == "XOR") {
				output.finalResult = input.finalResult;
			}
			if (operation.name() == "STORE") {
				inst[2] = inst[2].replaceAll("R", "");
				int source = regfile[Integer.parseInt(inst[2])];
				globals.memory[input.finalResult] = source;
				output.finalResult = input.finalResult;
				globals.register_invalid[Integer.parseInt(inst[2])] = false;
			}
			if (operation.name() == "LOAD") {
				int finalResult = globals.memory[input.finalResult];
				System.out.println("final Memory: " + finalResult);
				output.finalResult = finalResult;
			}

			// if(operation.name()=="STORE")
			// {
			// //System.out.println("Store Source1: "+ins.getSrc1().getValue());
			// //System.out.println("Store Source2: "+ins.getSrc2().getValue());
			// //System.out.println("Store Operation: "+ins.getOper0().getValue());
			//
			// int address=(ins.getSrc1().getValue()+ins.getSrc2().getValue());
			// regfile[address]=ins.getOper0().getValue();
			// System.out.println("Instruction stored: "+regfile[address]);
			//
			// }

			if (ins.isNull())
				return;

			// Access memory...
			// output.finalResult=input.finalResult;
			output.setInstruction(ins);
			// Set other data that's passed to the next stage.
		}
	}

	/*** Writeback Stage ***/
	static class Writeback extends PipelineStageBase<MemoryToWriteback, VoidLatch> {
		public Writeback(CpuCore core, PipelineRegister input, PipelineRegister output) {
			super(core, input, output);

		}

		@Override
		public void compute(MemoryToWriteback input, VoidLatch output) {
			InstructionBase ins = input.getInstruction();

			GlobalData globals = (GlobalData) core.getGlobalResources();
			// System.out.println("Input is: "+ins);
			System.out.println("Writeback ins:" + ins);

			String inst[];

			if (ins.isNull())
				return;
			else {
				inst = ins.toString().split(" ");
				inst[2] = inst[2].replaceAll("R", "");
			}
			// boolean operation = (ins.getOpcode().equals("MOVC"));
			EnumOpcode operation = ins.getOpcode();

			if (operation.name() == "MOVC") {
				globals.register_file[Integer.parseInt(inst[2])] = Integer.parseInt(inst[3]);
				globals.register_invalid[Integer.parseInt(inst[2])] = false;
				// flag_check=0;
			}
			if (operation.name() == "ADD") {
				globals.register_file[Integer.parseInt(inst[2])] = input.finalResult;
				globals.register_invalid[Integer.parseInt(inst[2])] = false;
				// flag_check=0;
			}
			if (operation.name() == "AND") {
				globals.register_file[Integer.parseInt(inst[2])] = input.finalResult;
				globals.register_invalid[Integer.parseInt(inst[2])] = false;
				// flag_check=0;
			}
			if (operation.name() == "OR") {
				globals.register_file[Integer.parseInt(inst[2])] = input.finalResult;
				globals.register_invalid[Integer.parseInt(inst[2])] = false;
				// flag_check=0;
			}
			if (operation.name() == "XOR") {
				globals.register_file[Integer.parseInt(inst[2])] = input.finalResult;
				globals.register_invalid[Integer.parseInt(inst[2])] = false;
				// flag_check=0;
			}
			if (operation.name() == "SUB") {
				globals.register_file[Integer.parseInt(inst[2])] = input.finalResult;
				globals.register_invalid[Integer.parseInt(inst[2])] = false;
				// flag_check=0;
			}
			if (operation.name() == "MULS") {
				globals.register_file[Integer.parseInt(inst[2])] = input.finalResult;
				globals.register_invalid[Integer.parseInt(inst[2])] = false;
				// flag_check=0;
			}
			if (operation.name() == "STORE") {
				for (int i = 0; i < globals.memory.length; i++)
					System.out.println("Memory " + i + ": Values: " + globals.memory[i]);
				// flag_check=0;
			}
			if (operation.name() == "LOAD") {
				inst[2] = inst[2].replaceAll("R", "");
				globals.register_file[Integer.parseInt(inst[2])] = input.finalResult;
				globals.register_invalid[Integer.parseInt(inst[2])] = false;
				// flag_check=0;
			}
			// Write back result to register file
			for (int i = 0; i < globals.register_file.length; i++)
				System.out.println("Register " + i + ": Values: " + globals.register_file[i]);

			if (input.getInstruction().getOpcode() == EnumOpcode.HALT) {
				// Stop the simulation
			}
		}
	}
}
