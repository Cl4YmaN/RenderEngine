package de.sebastiankings.renderengine.bo;

import java.util.Map;

public class OBJData {

	private OBJEntity entityData;
	private Map<String, OBJMaterial> materialData;

	public OBJData(OBJEntity entityData, Map<String, OBJMaterial> materialData) {
		super();
		this.entityData = entityData;
		this.materialData = materialData;
	}

	public OBJEntity getEntityData() {
		return entityData;
	}

	public Map<String, OBJMaterial> getMaterialData() {
		return materialData;
	}
	
	public OBJMaterial getMaterialByName(String name){
		if(materialData != null && !materialData.isEmpty()){
			return materialData.get(name);
		}
		System.out.println("Material not found! " + name);
		return null;
	}

}
