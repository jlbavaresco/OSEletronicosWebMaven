package br.edu.ifsul.controle;

import br.edu.ifsul.dao.MarcaDAO;
import br.edu.ifsul.dao.ProdutoDAO;
import br.edu.ifsul.modelo.Arquivo;
import br.edu.ifsul.modelo.Marca;

import br.edu.ifsul.modelo.Produto;
import br.edu.ifsul.util.Util;
import br.edu.ifsul.util.UtilRelatorios;
import java.io.Serializable;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Prof. Me. Jorge Luis Boeira Bavaresco
 * @email jorge.bavaresco@passofundo.ifsul.edu.br
 * @organization IFSUL - Campus Passo Fundo
 */
@Named(value = "controleProduto")
@ViewScoped
public class ControleProduto implements Serializable {

    @EJB
    private ProdutoDAO<Produto> dao;
    private Produto objeto;
    @EJB
    private MarcaDAO<Marca> daoMarca;
    private Arquivo arquivo;

    public ControleProduto() {

    }

    public void imprimirProdutos() {
        HashMap parametros = new HashMap();
        UtilRelatorios.imprimeRelatorio("relatorioProdutos", parametros, dao.getListaTodos());
    }

    public String listar() {
        return "/privado/produto/listar?faces-redirect=true";
    }

    public void novo() {
        objeto = new Produto();
    }

    public void alterar(Object id) {
        try {
            objeto = dao.getObjectById(id);
        } catch (Exception e) {
            Util.mensagemErro("Erro ao recuperar objeto: "
                    + Util.getMensagemErro(e));
        }
    }

    public void excluir(Object id) {
        try {
            objeto = dao.getObjectById(id);
            dao.remover(objeto);
            Util.mensagemInformacao("Objeto removido com sucesso!");
        } catch (Exception e) {
            Util.mensagemErro("Erro ao remover objeto: "
                    + Util.getMensagemErro(e));
        }
    }

    public void salvar() {
        try {
            if (objeto.getId() == null) {
                dao.persist(objeto);
            } else {
                dao.merge(objeto);
            }
            Util.mensagemInformacao("Objeto persistido com sucesso!");
        } catch (Exception e) {
            Util.mensagemErro("Erro ao persistir objeto: "
                    + Util.getMensagemErro(e));
        }
    }

    public void novoArquivo() {
        arquivo = new Arquivo();
    }

    public void salvarArquivo() {
        objeto.adicionarArquivo(arquivo);
        Util.mensagemInformacao("Arquivo adicionado com sucesso!");
    }

    public void removerArquivo(int index) {
        objeto.removerArquivo(index);
        Util.mensagemInformacao("Arquivo removido com sucesso!");
    }

    public void enviarArquivo(FileUploadEvent event) {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            FacesContext aFacesContext = FacesContext.getCurrentInstance();
            ServletContext context = (ServletContext) aFacesContext.getExternalContext().getContext();
            arquivo.setArquivo(event.getFile().getContents());
            String nomeArquivo = event.getFile().getFileName();
            nomeArquivo = nomeArquivo.replaceAll("[ ]", "_");
            arquivo.setNomeArquivo(nomeArquivo);
            Util.mensagemInformacao("Arquivo enviado com sucesso!");
        } catch (Exception e) {
            Util.mensagemErro("Erro ao enviar arquivo: " + Util.getMensagemErro(e));
        }
    }

    public void downloadVersao(int index) {
        arquivo = objeto.getArquivos().get(index);
        byte[] download = (byte[]) arquivo.getArquivo();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-Disposition", "attachment; filename=" + arquivo.getNomeArquivo());
        response.setContentLength(download.length);
        try {
            response.setContentType("application/octet-stream");
            response.getOutputStream().write(download);
            response.getOutputStream().flush();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            Util.mensagemErro("Erro no download do arquivo: " + Util.getMensagemErro(e));
        }
    }

    public ProdutoDAO getDao() {
        return dao;
    }

    public void setDao(ProdutoDAO dao) {
        this.dao = dao;
    }

    public Produto getObjeto() {
        return objeto;
    }

    public void setObjeto(Produto objeto) {
        this.objeto = objeto;
    }

    public MarcaDAO<Marca> getDaoMarca() {
        return daoMarca;
    }

    public void setDaoMarca(MarcaDAO<Marca> daoMarca) {
        this.daoMarca = daoMarca;
    }

    public Arquivo getArquivo() {
        return arquivo;
    }

    public void setArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }

}
