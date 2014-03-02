package data_structure;

import java.util.ArrayList;


public class FastStudent {
	public String name = "";
	public String file_location = "";
	public String link = "";
	public String overall_comment = "";
	public String overall_output = "";
	public float total = 0;
	public ArrayList<Float> q_grades = new ArrayList<Float>();
	public ArrayList<String> q_output = new ArrayList<String>();
	public ArrayList<Integer> q_maximum = new ArrayList<Integer>();
	public ArrayList<Boolean> p_exists = new ArrayList<Boolean>();
	public ArrayList<String> q_comments = new ArrayList<String>();
	public ArrayList<ArrayList<Float>> p_grades = new ArrayList<ArrayList<Float>>();
	public ArrayList<ArrayList<Integer>> p_maximum = new ArrayList<ArrayList<Integer>>();
	public ArrayList<ArrayList<String>> p_comments = new ArrayList<ArrayList<String>>();
		
	
	public FastStudent(String name){
		this.name = name;
	}
	
	public void loadQuestions(ArrayList<Integer> questions, ArrayList<Boolean> p_exists, ArrayList<Integer> q_maximum, ArrayList<ArrayList<Integer>> p_maximum){
		for(int ii = 0; ii < questions.size(); ii++){
			q_grades.add((float)0.0);
			q_comments.add("");
		}
		this.p_exists = p_exists;
		this.q_maximum = q_maximum;
		for(int ii = 0; ii < p_maximum.size(); ii++){
			ArrayList<Float> row = new ArrayList<Float>();
			ArrayList<String> comms = new ArrayList<String>();
			for(int jj = 0; jj < p_maximum.get(ii).size(); jj++){
				row.add((float)0.0);
				comms.add("");
			}
			p_grades.add(row);
			p_comments.add(comms);
		}
		this.p_maximum = p_maximum;
	
	}
	
	public void calculateTotal(){
		total = 0;
		for(int ii = 0; ii < q_grades.size(); ii++){
			if(p_exists.get(ii)){
				for(int kk = 0; kk < p_grades.get(ii).size(); kk++)
					total += p_grades.get(ii).get(kk);
			} else
				total += q_grades.get(ii);
		}
	}
	
	public void print(){
		System.out.println("\n" + name);
		System.out.println("file at " + file_location);
		System.out.println("link at " + link);
		System.out.println("total = " + total);
		System.out.println("Overall: " + overall_comment);
		for(int ii = 0; ii < q_grades.size(); ii++){
			if(p_exists.get(ii)){
				for(int jj = 0; jj < p_grades.get(ii).size(); jj++){
					System.out.println(Integer.toString(ii) + (char)('a' + jj) + " = " + p_grades.get(ii).get(jj) + " \t\t\t " + p_comments.get(ii).get(jj));
				}
			} else{
				System.out.println(Integer.toString(ii) + " = " + q_grades.get(ii) + "\t\t\t" + q_comments.get(ii));
			}
		}
		System.out.println("\n\n");
	}
	
	public void createOutput(){
		overall_output = "Total Grade: ";
		overall_output += Float.toString(total);
		overall_output += "/100\n\n";
		overall_output += overall_comment;
		for(int ii = 0; ii < q_grades.size(); ii++){
			String str = "";
			str += "Question " + Integer.toString(ii+1) + " Grade: ";
			float grade = 0;
			float max_grade = 0;
			if(p_exists.get(ii)){
				for(int jj = 0; jj < p_grades.get(ii).size(); jj++){
					grade += p_grades.get(ii).get(jj);
					max_grade += p_maximum.get(ii).get(jj);
				}
			} else{
				grade = q_grades.get(ii);
				max_grade = q_maximum.get(ii);
			}
			str += Float.toString(grade);
			str += "/" + Float.toString(max_grade) + "\n\n";
			if(p_exists.get(ii)){
				for(int jj = 0; jj < p_comments.get(ii).size(); jj++)
					str += p_comments + "\n";
			} else{
				str += q_comments.get(ii);
				String new_str = "";
				for(int zz = 0; zz < str.length()-1; zz++){
					if(str.charAt(zz) == '\\' && str.charAt(zz+1) == 'n'){
						new_str += '\n';
						new_str += "<br />";
						zz++;
					} else
						new_str += str.charAt(zz);
				}
				new_str += str.charAt(str.length()-1);
				str = new_str;
			}
			q_output.add(str);
		}
	}
}
