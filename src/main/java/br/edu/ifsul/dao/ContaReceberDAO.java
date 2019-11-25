package br.edu.ifsul.dao;

import br.edu.ifsul.converters.ConverterOrdem;
import br.edu.ifsul.modelo.ContaReceber;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateful;

/**
 * @author Prof. Me. Jorge Luis Boeira Bavaresco
 * @email jorge.bavaresco@passofundo.ifsul.edu.br
 * @organization IFSUL - Campus Passo Fundo
 */
@Stateful
public class ContaReceberDAO<TIPO> extends DAOGenerico<ContaReceber> implements Serializable {
    
    private Boolean filtroVencimento = false;
    private Calendar dataInicial;
    private Calendar dataFinal;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");	    

    public ContaReceberDAO(){
        super();
        classePersistente = ContaReceber.class;
        // inicializar as ordenações possiveis   
        // Necessário adicionar o obj pois tem que recuperar a ordem de serviço do id que ficou lazy no modelo
        listaOrdem.add(new Ordem("obj.id.ordemServico.id", "Ordem de Serviço", "="));
        listaOrdem.add(new Ordem("obj.id.ordemServico.pessoaFisica.nome", "Pessoa Física", "like"));
        // definir qual a ordenação padrão no caso o segundo elemento da lista (indice 1)
        ordemAtual = listaOrdem.get(0);
        // inicializar o conversor com a lista de ordens
        converterOrdem = new ConverterOrdem(listaOrdem);
    }
    
    @Override
    public List<ContaReceber> getListaObjetos() {        
        //necessario escrever a consulta com o fetch para inicializar o atributo ordemServico do id
        // para não dar erro de lazy inicialization exception na view
        String jpql = "select obj from " + classePersistente.getSimpleName() + " obj join fetch obj.id.ordemServico ";
        String where = "";
        // removendo caracteres para proteger de sql injection
        filtro = filtro.replaceAll("[';-]", "");
        if (filtro.length() > 0) {
           switch (ordemAtual.getOperador()) {
                case "=":
                    // tratamento para caso digitem com id selecionado algo que não é numero não gerar exceção
                    if (ordemAtual.getAtributo().equals("id")) {
                        try {
                            Integer i = Integer.parseInt(filtro);
                        } catch (Exception e) {
                            filtro = "0";
                        }
                    }
                    where += " where " + ordemAtual.getAtributo() + " = '" + filtro + "' ";
                    break;
                case "like":
                     where += " where upper(" + ordemAtual.getAtributo() + ") like '" + filtro.toUpperCase() + "%' ";
                    break;
            }                        
        }
        if (filtroVencimento){
            if (!(where.length() > 0)){
                where = " where ";
            } else {
                where += " and ";
            }
            where += " obj.vencimento >= '"+sdf.format(dataInicial.getTime())+ "' and obj.vencimento <= '"+
                    sdf.format(dataFinal.getTime())+ "' ";
        }        
        jpql += where;
        jpql += " order by " + ordemAtual.getAtributo();
        System.out.println("JPQL: " + jpql);
        totalObjetos = em.createQuery(jpql).getResultList().size();
        return em.createQuery(jpql).
                setFirstResult(posicaoAtual).
                setMaxResults(maximoObjetos).getResultList();
    }    

    public Boolean getFiltroVencimento() {
        return filtroVencimento;
    }

    public void setFiltroVencimento(Boolean filtroVencimento) {
        this.filtroVencimento = filtroVencimento;
    }

    public Calendar getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Calendar dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Calendar getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Calendar dataFinal) {
        this.dataFinal = dataFinal;
    }
}