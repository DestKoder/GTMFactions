package ru.dest.gtmfractions.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
public class Faction {

    @Getter
    private final String id;
    private final Map<String, FactionGroup> groups;

    public Collection<FactionGroup> listGroups(){
        return groups.values();
    }
    public Set<String> getGroups(){
        return groups.keySet();
    }

    public FactionGroup getGroup(String id){
        return groups.get(id);
    }

    public FactionGroup getFirstGroup(){
        for(FactionGroup group : groups.values()){
            if(group.getId().equals("leader"))continue;
            if(group.getPrev() != null) continue;

            return group;
        }
        return null;
    }
}
