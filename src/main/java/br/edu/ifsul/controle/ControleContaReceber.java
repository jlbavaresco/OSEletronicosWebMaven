package br.edu.ifsul.controle;


import br.edu.ifsul.dao.ContaReceberDAO;
import br.edu.ifsul.dao.EstadoDAO;


import br.edu.ifsul.modelo.ContaReceber;
import br.edu.ifsul.modelo.ContaReceberID;
import br.edu.ifsul.modelo.Estado;
import br.edu.ifsul.util.Util;
import br.edu.ifsul.util.UtilRelatorios;
import java.io.Serializable;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Prof. Me. Jorge Luis Boeira Bavaresco
 * @email jorge.bavaresco@passofundo.ifsul.edu.br
 * @organization IFSUL - Campus Passo Fundo
 */
@Named(value = "controleContaReceber")
@ViewScoped
public class ControleContaReceber implements Serializable {
    
    @EJB
    private ContaReceberDAO<ContaReceber> dao;
    private ContaReceber objeto;
 
    
    public ControleContaReceber(){
        
    }
    
    public void imprimirContas() {
        HashMap parametros = new HashMap();
        UtilRelatorios.imprimeRelatorio("relatorioContas", parametros, dao.getListaObjetos());
    }    
       
    public String listar(){
        return "/privado/contareceber/listar?faces-redirect=true";
    }
    
    public void editar(ContaReceberID id) {
        try {
            objeto = dao.getObjectById(id);
        } catch (Exception e) {
           Util.mensagemErro("Erro ao recuperar objeto: "
                    + Util.getMensagemErro(e));
        }
    }
    
    public void salvarPagamento() {
        try {
            dao.merge(objeto);
            Util.mensagemInformacao("Objeto persistido com sucesso");
        } catch (Exception e) {
            Util.mensagemErro("Erro ao persistir: "
                    + Util.getMensagemErro(e));
        }
    }    
    
 

    public ContaReceberDAO getDao() {
        return dao;
    }

    public void setDao(ContaReceberDAO dao) {
        this.dao = dao;
    }

    public ContaReceber getObjeto() {
        return objeto;
    }

    public void setObjeto(ContaReceber objeto) {
        this.objeto = objeto;
    }


}
