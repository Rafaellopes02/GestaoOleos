import React, { useEffect, useState } from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { TableVirtuoso } from 'react-virtuoso';
import { FaEye } from "react-icons/fa";
import Chip from '@mui/material/Chip';
import { jwtDecode } from "jwt-decode";

import DetalhesPagamento from './DetalhesPagamento';

const columns = [
    { width: 200, label: 'Contrato', dataKey: 'contrato' },
    { width: 150, label: 'Método', dataKey: 'metodo' },
    { width: 100, label: 'Valor', dataKey: 'valor' },
    { width: 150, label: 'Estado', dataKey: 'estado' },
    { width: 100, label: 'Ver', dataKey: 'ver' }
];

const VirtuosoTableComponents = {
    Scroller: React.forwardRef((props, ref) => (
        <TableContainer component={Paper} {...props} ref={ref} />
    )),
    Table: (props) => (
        <Table {...props} sx={{ borderCollapse: 'separate', tableLayout: 'fixed' }} />
    ),
    TableHead: React.forwardRef((props, ref) => (
        <TableHead {...props} ref={ref} />
    )),
    TableRow,
    TableBody: React.forwardRef((props, ref) => (
        <TableBody {...props} ref={ref} />
    )),
};

function fixedHeaderContent() {
    return (
        <TableRow>
            {columns.map((column) => (
                <TableCell
                    key={column.dataKey}
                    variant="head"
                    align="left"
                    style={{ width: column.width }}
                    sx={{ backgroundColor: 'background.paper', fontWeight: 'bold' }}
                >
                    {column.label}
                </TableCell>
            ))}
        </TableRow>
    );
}

export default function TablePagamentos() {
    const [rows, setRows] = useState([]);
    const [modalOpen, setModalOpen] = useState(false);
    const [modalPagarOpen, setModalPagarOpen] = useState(false);
    const [pagamentoSelecionado, setPagamentoSelecionado] = useState(null);
    const [metodosPagamento, setMetodosPagamento] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const token = localStorage.getItem("token");
                const decoded = jwtDecode(token);
                const idUtilizador = parseInt(decoded.sub, 10);

                const [pagamentos, contratos, metodos, estados] = await Promise.all([
                    fetch('http://localhost:8080/api/pagamentos').then(res => res.json()),
                    fetch('http://localhost:8080/Contratos/com-estado').then(res => res.json()),
                    fetch('http://localhost:8080/api/metodospagamento').then(res => res.json()),
                    fetch('http://localhost:8080/api/estadospagamento').then(res => res.json())
                ]);

                const contratosFiltrados = contratos.filter(c => c.idutilizador === idUtilizador);
                const contratosDoUtilizador = new Set(contratosFiltrados.map(c => c.idcontrato));

                const contratoMap = new Map(contratos.map(c => [c.idcontrato, c.nome]));
                const metodoMap = new Map(metodos.map(m => [m.idmetodopagamento, m.metodo]));
                const estadoMap = new Map(estados.map(e => [e.idestadospagamento, e.nome]));

                const data = pagamentos
                    .filter(p => contratosDoUtilizador.has(p.idcontrato))
                    .sort((a, b) => b.idpagamento - a.idpagamento)
                    .map(p => ({
                        contrato: contratoMap.get(p.idcontrato) || `#${p.idcontrato}`,
                        metodo: metodoMap.get(p.idmetodopagamento) || `#${p.idmetodopagamento}`,
                        valor: p.valor,
                        estado: estadoMap.get(Number(p.idestadospagamento)) || `#${p.idestadospagamento}`,
                        detalhes: p
                    }));

                setRows(data);
                setMetodosPagamento(metodos);
            } catch (error) {
                console.error("Erro ao carregar dados:", error);
            }
        };

        fetchData();
    }, []);

    const abrirDetalhes = (pagamento) => {
        setPagamentoSelecionado(pagamento);
        setModalOpen(true);
    };

    const fecharDetalhes = () => {
        setModalOpen(false);
        setPagamentoSelecionado(null);
    };

    const fecharPagar = () => {
        setModalPagarOpen(false);
        setPagamentoSelecionado(null);
    };

    const pagar = (idpagamento, idmetodo) => {
        console.log("Pagamento efetuado → ID:", idpagamento, " | Método:", idmetodo);
        fecharPagar();
    };

    const EstadoChip = ({ estado }) => {
        let estilo = {};

        if (estado === 'Concluido') {
            estilo = {
                backgroundColor: '#ccffcc',
                color: 'green',
                fontWeight: 'bold',
                borderRadius: '999px',
                padding: '0 10px'
            };
        } else if (estado === 'Pendente') {
            estilo = {
                backgroundColor: '#ffe6e6',
                color: 'red',
                fontWeight: 'bold',
                borderRadius: '999px',
                padding: '0 10px'
            };
        }

        return <Chip label={estado} style={estilo} />;
    };

    return (
        <>
            <Paper style={{ height: 400, width: '90%', margin: '30px auto' }}>
                <TableVirtuoso
                    data={rows}
                    components={VirtuosoTableComponents}
                    fixedHeaderContent={fixedHeaderContent}
                    itemContent={(index, row) => (
                        <>
                            <TableCell>{row.contrato}</TableCell>
                            <TableCell>{row.metodo}</TableCell>
                            <TableCell>{row.valor.toFixed(2)} €</TableCell>
                            <TableCell><EstadoChip estado={row.estado} /></TableCell>
                            <TableCell>
                                <FaEye style={{ cursor: 'pointer' }} onClick={() => abrirDetalhes(row)} />
                            </TableCell>
                        </>
                    )}
                />
            </Paper>

            <DetalhesPagamento
                open={modalOpen}
                onClose={fecharDetalhes}
                pagamento={pagamentoSelecionado}
            />
        </>
    );
}
