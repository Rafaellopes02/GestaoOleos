import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { FaEye } from 'react-icons/fa';

import { TableVirtuoso } from 'react-virtuoso';

const columns = [
    { width: 200, label: 'Contrato', dataKey: 'idcontrato' },
    { width: 150, label: 'Morada', dataKey: 'morada' },
    { width: 100, label: 'Data Recolha', dataKey: 'data' },
    { width: 100, label: 'Estado', dataKey: 'idestadorecolha' },
    { width: 50, label: 'Ver', dataKey: 'ver' },
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
                    align={column.numeric ? 'right' : 'left'}
                    style={{ width: column.width }}
                    sx={{ backgroundColor: 'background.paper', fontWeight: 'bold' }}
                >
                    {column.label}
                </TableCell>
            ))}
        </TableRow>
    );
}

function getEstadoEstilo(id) {
    switch (id) {
        case 1:
            return { texto: 'Pendente', estilo: { backgroundColor: '#b2c8ff', color: '#25324c' } };
        case 2:
            return { texto: 'Em Andamento', estilo: { backgroundColor: '#ede7f6', color: '#6a1b9a' } };
        case 3:
            return { texto: 'Concluído', estilo: { backgroundColor: '#ccffde', color: '#2e7d32' } };
        case 4:
            return { texto: 'Cancelado', estilo: { backgroundColor: '#fdecea', color: '#d32f2f' } };
        case 5:
            return { texto: 'Aguardando Coleta', estilo: { backgroundColor: '#fff4e5', color: '#ef6c00' } };
        default:
            return { texto: 'Desconhecido', estilo: { backgroundColor: '#eeeeee', color: '#757575' } };
    }
}

// Row content separado para poder receber função onVerClick
function rowContentFactory(onVerClick) {
    return (_index, row) => (
        <>
            {columns.map((column) => (
                <TableCell key={column.dataKey} align="left">
                    {column.dataKey === 'idestadorecolha' ? (
                        (() => {
                            const { texto, estilo } = getEstadoEstilo(row[column.dataKey]);
                            return (
                                <span
                                    style={{
                                        ...estilo,
                                        fontWeight: 'bold',
                                        padding: '6px 12px',
                                        borderRadius: '20px',
                                        display: 'inline-block',
                                        textAlign: 'center',
                                        minWidth: '120px',
                                    }}
                                >
                                    {texto}
                                </span>
                            );
                        })()
                    ) : column.dataKey === 'idcontrato' ? (
                        `${row.idcontrato} - ${row.nome}`
                    ) : column.dataKey === 'ver' ? (
                        <FaEye
                            style={{ cursor: 'pointer' }}
                            onClick={() => onVerClick(row)}
                        />
                    ) : (
                        row[column.dataKey]
                    )}
                </TableCell>
            ))}
        </>
    );
}

export default function TableContratos() {
    const [rows, setRows] = useState([]);
    const [modalAberto, setModalAberto] = useState(false);
    const [recolhaSelecionada, setRecolhaSelecionada] = useState(null);

    const abrirModal = (row) => {
        setRecolhaSelecionada(row);
        setModalAberto(true);
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const token = localStorage.getItem('token');
                const decoded = jwtDecode(token);
                const tipo = parseInt(decoded.tipo, 10);
                const idutilizador = parseInt(decoded.sub, 10);

                const [recolhasRes, contratosRes] = await Promise.all([
                    axios.get('http://localhost:8080/Recolhas', {
                        headers: { Authorization: `Bearer ${token}` }
                    }),
                    axios.get('http://localhost:8080/Contratos/com-estado', {
                        headers: { Authorization: `Bearer ${token}` }
                    }),
                ]);

                const contratosMap = new Map();
                contratosRes.data.forEach(c => {
                    contratosMap.set(c.idcontrato, { nome: c.nome, idutilizador: c.idutilizador });
                });

                let recolhasComNome = recolhasRes.data.map(r => {
                    const contratoInfo = contratosMap.get(r.idcontrato);
                    return {
                        ...r,
                        nome: contratoInfo ? contratoInfo.nome : 'Desconhecido'
                    };
                });

                if (tipo === 1) {
                    recolhasComNome = recolhasComNome.filter(r => r.idutilizador === idutilizador);
                }

                recolhasComNome.sort((a, b) => b.idrecolha - a.idrecolha);

                setRows(recolhasComNome);
            } catch (error) {
                console.error('Erro ao buscar dados:', error);
            }
        };

        fetchData();
    }, []);

    return (
        <>
            <Paper style={{ height: 400, width: '90%', margin: '30px auto' }}>
                <TableVirtuoso
                    data={rows}
                    components={VirtuosoTableComponents}
                    fixedHeaderContent={fixedHeaderContent}
                    itemContent={rowContentFactory(abrirModal)}
                />
            </Paper>

            {modalAberto && recolhaSelecionada && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Detalhes da Recolha</h2>
                        <p><strong>Contrato:</strong> {recolhaSelecionada.idcontrato} - {recolhaSelecionada.nome}</p>
                        <p><strong>Morada:</strong> {recolhaSelecionada.morada}</p>
                        <p><strong>Data:</strong> {recolhaSelecionada.data}</p>
                        <p><strong>Quantidade:</strong> {recolhaSelecionada.quantidade} ml</p>
                        <p><strong>Número de Bidões:</strong> {recolhaSelecionada.numbidoes}</p>
                        <p><strong>Observações:</strong> {recolhaSelecionada.observacoes || "Nenhuma"}</p>
                        <button onClick={() => setModalAberto(false)}>Fechar</button>
                    </div>
                </div>
            )}
        </>
    );
}
