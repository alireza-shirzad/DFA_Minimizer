# DFA_Minimizer
A tool for minimizing any given DFA (Deterministic finite automaton)

## Dependencies
No Dependency needed.

## Input format
The input DFA should be formatted in the following way:
#NumOfStates #SizeOfAlphabet
{Final States}
{
Transitions
}

### For example
Consider the following DFA:
<br>
<img src="https://github.com/alireza-shirzad/DFA_Minimizer/blob/main/DFA_Image.svg" align="center" height="200" width="300" >
<br>
The equivalent Input is:
<br>

2 2
<br>
1
<br>
2 1
<br>
1 2

### Be careful
* States start from number 1
* State 1 is always the start state

## Output format
The same as the input format

## Algorithm
Steps:
* Removing Dead states (non-final states that only transit to themselves)
* Removing Inaccessible states (States that can not be reached from intial states)
* Performing Myphill-Nerode algorithm using Java data structures
