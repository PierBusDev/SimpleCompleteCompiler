package CodeGeneration.simpleProgLang;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    Map<String, Integer> OffsetMap = new HashMap<>();

    public void insert( String s, int address){
        if(!OffsetMap.containsValue(address)){
            OffsetMap.put(s, address);
        }else{
            throw new IllegalArgumentException("This memory location is already occupied");
        }
    }

    public int lookupAddress(String s){
        if(OffsetMap.containsKey(s)){
            return OffsetMap.get(s);
        }else{
            return -1;
        }
    }
}
