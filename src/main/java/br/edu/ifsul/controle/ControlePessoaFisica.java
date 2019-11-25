package br.edu.ifsul.controle;

import br.edu.ifsul.dao.CidadeDAO;
import br.edu.ifsul.dao.PermissaoDAO;
import br.edu.ifsul.dao.PessoaFisicaDAO;
import br.edu.ifsul.modelo.Cidade;
import br.edu.ifsul.modelo.Permissao;

import br.edu.ifsul.modelo.PessoaFisica;
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
@Named(value = "controlePessoaFisica")
@ViewScoped
public class ControlePessoaFisica implements Serializable {
    
    @EJB
    private PessoaFisicaDAO<PessoaFisica> dao;
    private PessoaFisica objeto;
    private Boolean novoObjeto;
    @EJB
    private PermissaoDAO<Permissao> daoPermissao;
    private Permissao permissao;
    @EJB
    private CidadeDAO<Cidade> daoCidade;
    
    public ControlePessoaFisica() {
        
    }
    
    public String listar() {
        novoObjeto = false;
        return "/privado/pessoafisica/listar?faces-redirect=true";
    }
    
    public void novo() {
        novoObjeto = true;
        objeto = new PessoaFisica();        
    }
    
    public void alterar(Object id) {
        try {
            novoObjeto = false;
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
            if (novoObjeto) {
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
    
    public void salvarPermissao() {
        if (!objeto.getPermissoes().contains(permissao)) {
            objeto.getPermissoes().add(permissao);            
            Util.mensagemInformacao("Permissão adicionada com sucesso!");            
        } else {
            Util.mensagemErro("Usuário já possui esta permissão!");
        }
        
    }
    
    public void excluirPermissao(Permissao obj) {
        objeto.getPermissoes().remove(obj);        
        Util.mensagemInformacao("Permissão removida com sucesso!");        
    }
    
    public PessoaFisicaDAO getDao() {
        return dao;
    }
    
    public void setDao(PessoaFisicaDAO dao) {
        this.dao = dao;
    }
    
    public PessoaFisica getObjeto() {
        return objeto;
    }
    
    public void setObjeto(PessoaFisica objeto) {
        this.objeto = objeto;
    }
    
    public Boolean getNovoObjeto() {
        return novoObjeto;
    }
    
    public void setNovoObjeto(Boolean novoObjeto) {
        this.novoObjeto = novoObjeto;
    }
    
    public PermissaoDAO<Permissao> getDaoPermissao() {
        return daoPermissao;
    }
    
    public void setDaoPermissao(PermissaoDAO<Permissao> daoPermissao) {
        this.daoPermissao = daoPermissao;
    }
    
    public Permissao getPermissao() {
        return permissao;
    }
    
    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public CidadeDAO<Cidade> getDaoCidade() {
        return daoCidade;
    }

    public void setDaoCidade(CidadeDAO<Cidade> daoCidade) {
        this.daoCidade = daoCidade;
    }
    
}
