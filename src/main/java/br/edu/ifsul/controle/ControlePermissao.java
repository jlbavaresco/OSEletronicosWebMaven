package br.edu.ifsul.controle;


import br.edu.ifsul.dao.PermissaoDAO;


import br.edu.ifsul.modelo.Permissao;
import br.edu.ifsul.util.Util;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Prof. Me. Jorge Luis Boeira Bavaresco
 * @email jorge.bavaresco@passofundo.ifsul.edu.br
 * @organization IFSUL - Campus Passo Fundo
 */
@Named(value = "controlePermissao")
@ViewScoped
public class ControlePermissao implements Serializable {
    
    @EJB
    private PermissaoDAO<Permissao> dao;
    private Permissao objeto;
    private Boolean novoObjeto;
    
    public ControlePermissao(){
        
    }
    
   
    public String listar(){
        novoObjeto = false;
        return "/privado/permissao/listar?faces-redirect=true";
    }
    
    public void novo(){
        novoObjeto = true;
        objeto = new Permissao();        
    }
    

    
    public void alterar(Object id){
        try {
            novoObjeto = false;
            objeto = dao.getObjectById(id);            
        } catch (Exception e){
            Util.mensagemErro("Erro ao recuperar objeto: " + 
                    Util.getMensagemErro(e));
        } 
    }
    
    public void excluir(Object id){
        try {
            objeto = dao.getObjectById(id);
            dao.remover(objeto);
            Util.mensagemInformacao("Objeto removido com sucesso!");
        } catch (Exception e){
            Util.mensagemErro("Erro ao remover objeto: " + 
                    Util.getMensagemErro(e));
        }
    }
    
    public void salvar(){
        try {
            if (novoObjeto){
              dao.persist(objeto);  
            } else {
              dao.merge(objeto);  
            }
            
            Util.mensagemInformacao("Objeto persistido com sucesso!");            
        } catch(Exception e){
            Util.mensagemErro("Erro ao persistir objeto: " + 
                    Util.getMensagemErro(e));
        }
    }

    public PermissaoDAO getDao() {
        return dao;
    }

    public void setDao(PermissaoDAO dao) {
        this.dao = dao;
    }

    public Permissao getObjeto() {
        return objeto;
    }

    public void setObjeto(Permissao objeto) {
        this.objeto = objeto;
    }

    public Boolean getNovoObjeto() {
        return novoObjeto;
    }

    public void setNovoObjeto(Boolean novoObjeto) {
        this.novoObjeto = novoObjeto;
    }



}
