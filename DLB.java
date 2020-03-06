public class DLB implements DictInterface {
private trieNode head;
    public DLB(){

    }
    public boolean add(String s) {
      return add(s, this.head);
    }

    public boolean add(String s, trieNode head){ //Add to tree
        char[] chars = s.toCharArray();             

        if(chars.length == 0){                  //At end of word so add child with word marker *
            head.child = new trieNode('*');
            return true;
        }
        if(this.head == null){                  //First node in entire tree
            this.head = new trieNode(chars[0]);
            if(chars.length>1)
            return add(s.substring(1,s.length()),this.head);    //If more than 1 letter to this keep adding
            else
             return add("",this.head);                          //Add the special character
        }
        for(trieNode n = head; n!=null; n=n.sibling){       
            if(n.letter == chars[0]){                   //If at correct child
                if(n.child!=null){                      //If child is not null aka letter already there return the string being added with 1 less character
                if(chars.length>1)
                return add(s.substring(1,s.length()), n.child);
                else
                    return add("",n.child);         //End of word procedure
                }
                else{                           //Otherwise if at null child add every letter left
                    if(chars.length>1){
                        for(int i =1; i<chars.length;i++){
                            n.child = new trieNode(chars[i]);
                            n = n.child;
                        }

                    }
                return add("",n);
                }
            }
            if(n.sibling == null){          //If node we are on does not have a sibling then add sibling and its children
                n.sibling = new trieNode(chars[0]);
                n=n.sibling;
                if(chars.length>1){
                    for(int i =1; i<chars.length;i++){
                        n.child = new trieNode(chars[i]);
                        n = n.child;
                    }
                }
                return add("",n);
        }
    }
        
    return false;
    }

    @Override
    public int searchPrefix(StringBuilder s) {
       return searchPrefix(s,0,s.length()-1);
    }
    @Override
    public int searchPrefix(StringBuilder s, int start, int end) {      
        if(head == null){ 
            return 0;
        }
        char[] ayyyMustBeTheMoney = s.toString().toCharArray();
        int count = start;                                          //Follow letters down the tree essentially
        for(trieNode n = head; n!=null; n = n.child){                   
            for(;n!=null; n=n.sibling){
                if(count<=end&&n.letter == ayyyMustBeTheMoney[count]){
                    if(count!=end){
                        count++;
                        break;
                    }
                    else{
                        if(n.child!=null&&n.child.letter=='*'){
                            if(n.child.sibling!=null) return 3;     //Word+Prefix
                            else return 2;                          //Word only
                        }
                        else if(n.child!=null) return 1;            //Prefix only
                        else return 0;                  //Nothing
                    }
                }
                if(n.sibling == null) return 0;
            }
        }
        return 0;
    }

    private class trieNode{             //Node class
        private char letter;
        private trieNode sibling;
        private trieNode child;
        public trieNode(){
            letter = ' ';
            sibling = null;
            child = null;
        }
        public trieNode(char c){
            letter = c;
            sibling = null;
            child = null;
    }
}

}