package com.ha.model;

import com.ha.model.Category;

public class RootCategory extends Category {
	 public String getName() {
		 switch (getType()) {
		 case ASSET: return "Activo";
		 case LIABILITY: return "Pasivo";
		 case REVENUE: return "Ganancias";
		 case EXPENSE: return "Perdidas";
		 case EQUITY: return "Patrimonio";
		 default: return "Categoria Invalida";
		 }
	 }
}
