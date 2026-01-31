package Mastermind;

public class CodeGen
{
    String codeGen ()//creates a random code with repeats
    {
        String code = "";
        for (int i = 0 ; i < 4 ; i ++)
        {
            int number = (int)Math.floor((Math.random()*100000000)%6)+1;
            code = code+number;
        }
        return code;
    }
    
    String codeGenNoRep ()//creates a random code without repeats
    {
        String code = "";
        while (code.length()<4)
        {
            int number = (int)Math.floor((Math.random()*100000000)%6)+1;
            if (isAbsent(code,(char)(number+48)))
                code = code+number;
        }
        return code;
    }
    
    boolean isAbsent (String s, char c)//checks if a given character is absent in a given string
    {
        for (int i = 0 ; i < s.length() ; i++)
        {
            if (s.charAt(i)==c)
                return false;
        }
        return true;
    }
}
