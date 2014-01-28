package data_structure;

import java.util.ArrayList;

import logging.Log;

public class Student {
	public String name;
	public boolean submission;
	
	public String file_location;
	public String link;
	
	private Log ll;
	
	public float total;
	
	ArrayList<Question> questions;
	
	
	public Student(String name, boolean submission, Log ll){
		this.name = name;
		this.submission = submission;
		this.ll = ll;
		total = 0;
		questions = new ArrayList<Question>();
	}
	
	public void print(){
		ll.write(2, "\r\n\r\n");
		ll.write(2, "Student name = " + name + "\r\n");
		ll.write(2, "Student submitted = " + submission + "\r\n");
		ll.write(2, "Student file location = " + file_location + "\r\n");
		ll.write(2, "Student TWiki link location = " + link + "\r\n");
		
		ll.write(2, "\r\n\r\n");
	}
	
	public void addQuestion(int question_number, boolean parts, boolean extra_credit, float maximum_total){
		questions.add(new Question(question_number, parts, extra_credit, maximum_total, ll));
	}
	
	public void addPart(int question_number, char part_number, float maximum_grade, boolean extra_credit){
		boolean found = false;
		for(int ii = 0; ii < questions.size(); ii++){
			if(questions.get(ii).question_number == question_number){
				found = true;
				questions.get(ii).addPart(part_number, maximum_grade, extra_credit);
				break;
			}
		}
		if(!found){
			ll.write(1, "Question " + question_number + " is not found\r\n");
			System.exit(-1);
		}
	}

	public void set_grade_question(int question_number, float grade){
		boolean found = false;
		for(int ii = 0; ii < questions.size(); ii++){
			if(questions.get(ii).question_number == question_number){
				found = true;
				questions.get(ii).setGrade(grade);
				break;
			}
		}
		if(!found){
			ll.write(1, "Question " + question_number + " is not found\r\n");
			System.exit(-1);
		}
	}
	
	public void set_grade_part(int question_number, char part_number, float grade){
		boolean found = false;
		for(int ii = 0; ii < questions.size(); ii++){
			if(questions.get(ii).question_number == question_number){
				found = true;
				questions.get(ii).setGrade_part(part_number, grade);
				break;
			}
		}
		if(!found){
			ll.write(1, "Question " + question_number + " is not found\r\n");
			System.exit(-1);
		}
	}
	
	public float get_grade_question(int question_number){
		for(int ii = 0; ii < questions.size(); ii++){
			if(questions.get(ii).question_number == question_number)
				return questions.get(ii).current_total;
		}
		ll.write(1, "Question " + question_number + " is not found\r\n");
		System.exit(-1);
		return -1;
	}
	
	public float get_grade_part(int question_number, char part_number){
		for(int ii = 0; ii < questions.size(); ii++){
			if(questions.get(ii).question_number == question_number)
				return questions.get(ii).get_part_grade(part_number);
		}
		ll.write(1, "Question " + question_number + " is not found\r\n");
		System.exit(-1);
		return -1;
	}
}

class Question{
	public int question_number;
	public boolean parts;
	public boolean extra_credit;
	public float maximum_total;
	public float current_total;
	
	private Log ll;
	private ArrayList<Part> part;
	
	public Question(int q_n, boolean p, boolean e_c, float m_t, Log ll){
		question_number = q_n;
		parts = p;
		extra_credit = e_c;
		maximum_total = m_t;
		current_total = 0;
		part = new ArrayList<Part>();
		this.ll = ll;
	}
	
	public void addPart(char part_number, float maximum_grade, boolean extra_credit){
		part.add(new Part(part_number, maximum_grade, extra_credit));
		parts = true;
	}
	
	public void setGrade_part(char part_number, float grade){
		ll.write(2, "Setting grade of part " + part_number + " of question " + question_number + "\r\n");
		boolean found = false;
		for(int ii = 0; ii < part.size(); ii++){
			if(part.get(ii).part_number == part_number){
				found = true;
				part.get(ii).current_grade = grade;
				if(part.get(ii).maximum_grade < grade)
					ll.write(3, "Setting current_grade of part " + part_number + " to " + grade + " when the maximum grade is " + part.get(ii).maximum_grade + "\r\n");
				current_total += grade;
				if(current_total > maximum_total)
					ll.write(3, "Setting current_grade of question " + question_number + " to " + current_total + " which is greater than the maximum grade " + maximum_total + "\r\n");
				break;
			}
		}
		if(!found){
			ll.write(1, "Error! Tried to set grade " + grade + "to invalid part " + part);
			System.exit(-1);
		}
	}
	
	public void setGrade(float grade){
		ll.write(2, "Setting grade of question " + question_number + "\r\n");
		current_total = grade;
		if(current_total > maximum_total)
			ll.write(3, "Setting current_grade of question " + question_number + " to " + current_total + " which is greater than the maximum grade " + maximum_total + "\r\n");
	}
	
	public float get_part_grade(char part_number){
		ll.write(2, "Getting grade of part " + part_number + " of question " + question_number + "\r\n");
		for(int ii = 0; ii < part.size(); ii++){
			if(part.get(ii).part_number == part_number){
				return part.get(ii).current_grade;
			}
		}
		ll.write(1, "Part not found!");
		System.exit(-1);
		return -1;
	}
}

class Part{
	public char part_number;
	public float maximum_grade;
	public float current_grade;
	public boolean extra_credit;
	
	public Part(char p_n, float m_g, boolean e_c){
		part_number = p_n;
		maximum_grade = m_g;
		extra_credit = e_c;
		current_grade = 0;
	}
}
