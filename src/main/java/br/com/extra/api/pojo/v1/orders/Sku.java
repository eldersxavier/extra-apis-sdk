package br.com.extra.api.pojo.v1.orders;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Sku implements Serializable {

	private static final long serialVersionUID = -6439796042038845047L;

	private String skuId;
	private String skuName;
	private Double salePrice;
	private Integer quantity;

	public Sku() {
		super();

	}

	public Sku(String skuId, String skuName, Double salePrice, Integer quantity) {
		super();
		this.skuId = skuId;
		this.skuName = skuName;
		this.salePrice = salePrice;
		this.quantity = quantity;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
