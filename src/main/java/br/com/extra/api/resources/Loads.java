package br.com.extra.api.resources;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import br.com.extra.api.core.AppToken;
import br.com.extra.api.core.AuthToken;
import br.com.extra.api.core.CoreAPIImpl;
import br.com.extra.api.core.Hosts;
import br.com.extra.api.pojo.loads.ProductLoad;
import br.com.extra.api.utils.Utils;

import com.sun.jersey.api.client.ClientResponse;

/**
 * 
 * ExtraAPI-SDK - Loads.java
 * 
 * Implementação do Serviço Restful /loads.
 * 
 * Serviço que possibilita ao lojista realizar cargas.
 * 
 * @author Gibson Pasquini Nascimento
 * 
 *         17/07/2013
 */
public class Loads extends CoreAPIImpl<ProductLoad> implements LoadsResource {

	/**
	 * Construtor que instancia um objeto do serviço que consome a API /loads.
	 * 
	 * @param host
	 *            Host do serviço.
	 * @param appToken
	 *            Token de Aplicação.
	 * @param authToken
	 *            Token de Autenticação.
	 */
	public Loads(Hosts host, AppToken appToken, AuthToken authToken) {
		super(host, appToken, authToken);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<ProductLoad> getPojoClass() {
		return ProductLoad.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public String loadProducts(ProductLoad products) {

		setResource("/loads/products");

		byte[] compressedByteArray = compress(products);

		ClientResponse response = null;
		try {
			response = setMediaType("application/gzip").post(
					compressedByteArray);
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while trying to execute POST method on resource: "
							+ super.getURI());
		}

		if (response.getStatus() != ClientResponse.Status.CREATED
				.getStatusCode()) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.toString());
		}
		String resp = response.getStatus() + " - "
				+ response.getClientResponseStatus().getReasonPhrase()
				+ " location: " + response.getLocation();
		return resp;
	}

	/**
	 * Método utilizado para comprimir o conteúdo do request.
	 * 
	 * O conteúdo pode ser um arquivo JSON ou uma String que contém o JSON.
	 * 
	 * @param products
	 *            Objeto que contém o arquivo JSON ou a String que deverá ser
	 *            compactada.
	 * @return Array de bytes compactado.
	 */
	private byte[] compress(ProductLoad products) {
		// Array de bytes que será enviado para o serviço
		byte[] compressedByteArray = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzos = new GZIPOutputStream(baos);
			byte[] bytesToCompress = null;

			// Tratamento para compactar o arquivo JSON
			if (!Utils.isEmpty(products.getJsonFile())) {
				// Criação do InputStream do Arquivo
				FileInputStream stream = new FileInputStream(
						products.getJsonFile());
				byte[] buffer = new byte[8192];
				int bytesRead;
				// Conversão do arquivo em um Array de Bytes para ser compactado
				// via GZip
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				while ((bytesRead = stream.read(buffer)) != -1) {
					output.write(buffer, 0, bytesRead);
				}
				bytesToCompress = output.toByteArray();
				// Tratamento para compactar a String que contém o JSON
			} else if (!Utils.isEmpty(products.getProductsJson())) {
				bytesToCompress = products.getProductsJson().getBytes();
				// Lançamento de Exceção caso não seja enviado nem o arquivo nem a String.
			} else {
				throw new RuntimeException(
						"Error while trying gziping content. There is no content to be compressed.");
			}

			// Compactação do Array de Bytes
			gzos.write(bytesToCompress);

			gzos.finish();
			gzos.close();

			// Recuperação do Array de Bytes
			compressedByteArray = baos.toByteArray();
			baos.close();

		} catch (IOException e) {
			throw new RuntimeException(
					"Error while trying gziping content. Error: "
							+ e.getMessage());
		}
		return compressedByteArray;
	}

}