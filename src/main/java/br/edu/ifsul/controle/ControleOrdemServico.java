package br.edu.ifsul.controle;

import br.edu.ifsul.dao.EquipamentoDAO;
import br.edu.ifsul.dao.OrdemServicoDAO;
import br.edu.ifsul.dao.PessoaFisicaDAO;
import br.edu.ifsul.dao.ProdutoDAO;
import br.edu.ifsul.dao.ServicoDAO;
import br.edu.ifsul.dao.UsuarioDAO;
import br.edu.ifsul.modelo.Arquivo;
import br.edu.ifsul.modelo.ContaReceber;
import br.edu.ifsul.modelo.Equipamento;
import br.edu.ifsul.modelo.FormaPagamento;
import br.edu.ifsul.modelo.Foto;
import br.edu.ifsul.modelo.ItemProduto;
import br.edu.ifsul.modelo.ItemServico;

import br.edu.ifsul.modelo.OrdemServico;
import br.edu.ifsul.modelo.PessoaFisica;
import br.edu.ifsul.modelo.Produto;
import br.edu.ifsul.modelo.Servico;
import br.edu.ifsul.modelo.Status;
import br.edu.ifsul.modelo.Usuario;
import br.edu.ifsul.util.Util;
import br.edu.ifsul.util.UtilRelatorios;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Prof. Me. Jorge Luis Boeira Bavaresco
 * @email jorge.bavaresco@passofundo.ifsul.edu.br
 * @organization IFSUL - Campus Passo Fundo
 */
@Named(value = "controleOrdemServico")
@SessionScoped
public class ControleOrdemServico implements Serializable {

    @EJB
    private OrdemServicoDAO<OrdemServico> dao;
    private OrdemServico objeto;
    @EJB
    private UsuarioDAO<Usuario> daoUsuario;
    @EJB
    private PessoaFisicaDAO<PessoaFisica> daoPessoaFisica;
    @EJB
    private ServicoDAO<Servico> daoServico;
    @EJB
    private EquipamentoDAO<Equipamento> daoEquipamento;
    @EJB
    private ProdutoDAO<Produto> daoProduto;
    private ItemServico itemServico;
    private Boolean novoItemServico;
    private Foto foto;
    private ItemProduto itemProduto;
    private Boolean novoItemProduto;

    public ControleOrdemServico() {

    }

    public String listar() {
        return "/privado/ordemservico/listar?faces-redirect=true";
    }

    public void imprimeOS(Integer id) {
        try {
            objeto = dao.getObjectById(id);
            // Deve-se criar uma lista com um único objeto OS
            List<OrdemServico> listaOS = new ArrayList<>();
            listaOS.add(objeto);
            HashMap parametros = new HashMap();
            UtilRelatorios.imprimeRelatorio("relatorioOrdemServico", parametros, listaOS);
        } catch (Exception e) {
            System.out.println("Erro do imprimeOS: " + e.getMessage());
            e.printStackTrace();
            Util.mensagemErro("Erro ao imprimir: "
                    + Util.getMensagemErro(e));
        }
    }

    public void novo() {
        objeto = new OrdemServico();
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

    public void novoItemServico() {
        itemServico = new ItemServico();
        novoItemServico = true;
    }

    public void alterarItemServico(int index) {
        itemServico = objeto.getListaServicos().get(index);
        novoItemServico = false;
    }

    public void salvarItemServico() {
        if (novoItemServico) {
            objeto.adicionarServico(itemServico);
        } else {
            atualizarValorTotal();
        }
        Util.mensagemInformacao("Serviço adicionado com sucesso");
    }

    public void removerItemServico(int index) {
        objeto.removerServico(index);
        Util.mensagemInformacao("Serviço removido com sucesso");
    }

    public void atualizaValorUnitarioItemServico() {
        if (itemServico != null) {
            if (itemServico.getServico() != null) {
                itemServico.setValorUnitario(itemServico.getServico().getValor());
            }
        }
    }

    public void calculaValorTotalItemServico() {
        if (itemServico.getValorUnitario() != null
                && itemServico.getQuantidade() != null) {
            itemServico.setValorTotal(itemServico.getValorUnitario() * itemServico.getQuantidade());
        }
    }

    private void atualizarValorTotal() {
        objeto.setValorTotal(0.0);
        Double total = 0.0;
        for (ItemServico is : objeto.getListaServicos()) {
            total += is.getValorTotal();
        }
        for (ItemProduto ip : objeto.getListaProdutos()) {
            total += ip.getValorTotal();
        }

        objeto.setValorTotal(total);
    }

    public void novoItemProduto() {
        itemProduto = new ItemProduto();
        novoItemProduto = true;
    }

    public void alterarItemProduto(int index) {
        itemProduto = objeto.getListaProdutos().get(index);
        novoItemProduto = false;
    }

    public void salvarItemProduto() {
        if (novoItemProduto) {
            objeto.adicionarProduto(itemProduto);
        } else {
            atualizarValorTotal();
        }
        Util.mensagemInformacao("Produto adicionado com sucesso");
    }

    public void removerItemProduto(int index) {
        objeto.removerProduto(index);
        Util.mensagemInformacao("Produto removido com sucesso");
    }

    public void atualizaValorUnitarioItemProduto() {
        if (itemProduto != null) {
            if (itemProduto.getProduto() != null) {
                itemProduto.setValorUnitario(itemProduto.getProduto().getPreco());
            }
        }
    }

    public void calculaValorTotalItemProduto() {
        if (itemProduto.getValorUnitario() != null
                && itemProduto.getQuantidade() != null) {
            itemProduto.setValorTotal(itemProduto.getValorUnitario() * itemProduto.getQuantidade());
        }
    }

    public void gerarParcelas() {
        Boolean temPagamento = false;
        for (ContaReceber c : objeto.getContasReceber()) {
            if (c.getDataPagamento() != null || c.getValorPago() != null) {
                temPagamento = true;
            }
        }
        if (temPagamento) {
            Util.mensagemErro("Parcelas não podem ser geradas novamente "
                    + "por já existir pelo menos um pagamento");
        } else {
            objeto.getContasReceber().clear();
            objeto.gerarContasReceber();
            Util.mensagemInformacao("Parcelas Geradas com sucesso");
        }
    }

    public void novaFoto() {
        foto = new Foto();
    }

    public void salvarFoto() {
        objeto.adicionarFoto(foto);
        Util.mensagemInformacao("Foto adicionada com sucesso!");
    }

    public void removerFoto(int index) {
        objeto.removerFoto(index);
        Util.mensagemInformacao("Foto removida com sucesso!");
    }

    public void enviarFoto(FileUploadEvent event) {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            FacesContext aFacesContext = FacesContext.getCurrentInstance();
            ServletContext context = (ServletContext) aFacesContext.getExternalContext().getContext();
            foto.setArquivo(event.getFile().getContents());
            String nomeFoto = event.getFile().getFileName();
            nomeFoto = nomeFoto.replaceAll("[ ]", "_");
            foto.setNomeFoto(nomeFoto);
            Util.mensagemInformacao("Foto enviada com sucesso!");
        } catch (Exception e) {
            Util.mensagemErro("Erro ao enviar foto: " + Util.getMensagemErro(e));
        }
    }

    public void downloadFoto(int index) {
        foto = objeto.getFotos().get(index);
        byte[] download = (byte[]) foto.getArquivo();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-Disposition", "attachment; filename=" + foto.getNomeFoto());
        response.setContentLength(download.length);
        try {
            response.setContentType("application/octet-stream");
            response.getOutputStream().write(download);
            response.getOutputStream().flush();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            Util.mensagemErro("Erro no download da foto: " + Util.getMensagemErro(e));
        }
    }

    public void visualizarFoto(int index) {
        foto = objeto.getFotos().get(index);
    }

    public StreamedContent getImagemDinamica() {
        if (foto != null) {
            return new DefaultStreamedContent(new ByteArrayInputStream(foto.getArquivo()), "");
        } else {
            return new DefaultStreamedContent();
        }
    }

    public OrdemServicoDAO getDao() {
        return dao;
    }

    public void setDao(OrdemServicoDAO dao) {
        this.dao = dao;
    }

    public OrdemServico getObjeto() {
        return objeto;
    }

    public void setObjeto(OrdemServico objeto) {
        this.objeto = objeto;
    }

    public UsuarioDAO<Usuario> getDaoUsuario() {
        return daoUsuario;
    }

    public void setDaoUsuario(UsuarioDAO<Usuario> daoUsuario) {
        this.daoUsuario = daoUsuario;
    }

    public PessoaFisicaDAO<PessoaFisica> getDaoPessoaFisica() {
        return daoPessoaFisica;
    }

    public void setDaoPessoaFisica(PessoaFisicaDAO<PessoaFisica> daoPessoaFisica) {
        this.daoPessoaFisica = daoPessoaFisica;
    }

    public ServicoDAO<Servico> getDaoServico() {
        return daoServico;
    }

    public void setDaoServico(ServicoDAO<Servico> daoServico) {
        this.daoServico = daoServico;
    }

    public EquipamentoDAO<Equipamento> getDaoEquipamento() {
        return daoEquipamento;
    }

    public void setDaoEquipamento(EquipamentoDAO<Equipamento> daoEquipamento) {
        this.daoEquipamento = daoEquipamento;
    }

    public ProdutoDAO<Produto> getDaoProduto() {
        return daoProduto;
    }

    public void setDaoProduto(ProdutoDAO<Produto> daoProduto) {
        this.daoProduto = daoProduto;
    }

    public ItemServico getItemServico() {
        return itemServico;
    }

    public void setItemServico(ItemServico itemServico) {
        this.itemServico = itemServico;
    }

    public Boolean getNovoItemServico() {
        return novoItemServico;
    }

    public void setNovoItemServico(Boolean novoItemServico) {
        this.novoItemServico = novoItemServico;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public ItemProduto getItemProduto() {
        return itemProduto;
    }

    public void setItemProduto(ItemProduto itemProduto) {
        this.itemProduto = itemProduto;
    }

    public Boolean getNovoItemProduto() {
        return novoItemProduto;
    }

    public void setNovoItemProduto(Boolean novoItemProduto) {
        this.novoItemProduto = novoItemProduto;
    }

}
