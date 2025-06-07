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

import { TableVirtuoso } from 'react-virtuoso';

const columns = [
    { width: 200, label: 'Nome', dataKey: 'nome' },
    { width: 150, label: 'Data Início', dataKey: 'dataInicio' },
    { width: 150, label: 'Data Fim', dataKey: 'dataFim' },
    { width: 100, label: 'Estado', dataKey: 'estado' },
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

function getEstadoStyle(estado) {
    switch (estado.toLowerCase()) {
        case 'ativo':
            return {
                backgroundColor: '#e6f4ea',
                color: '#2e7d32',
                fontWeight: 'bold',
                padding: '6px 12px',
                borderRadius: '20px',
                display: 'inline-block',
                textAlign: 'center',
                minWidth: '80px',
            };
        case 'suspenso':
            return {
                backgroundColor: '#fff4e5',
                color: '#efbf00',
                fontWeight: 'bold',
                padding: '6px 12px',
                borderRadius: '20px',
                display: 'inline-block',
                textAlign: 'center',
                minWidth: '80px',
            };
        case 'encerrado':
            return {
                backgroundColor: '#fdecea',
                color: '#ff7300',
                fontWeight: 'bold',
                padding: '6px 12px',
                borderRadius: '20px',
                display: 'inline-block',
                textAlign: 'center',
                minWidth: '80px',
            };
        case 'não aceite':
            return {
                backgroundColor: '#fdecea',
                color: '#ff0000',
                fontWeight: 'bold',
                padding: '6px 12px',
                borderRadius: '20px',
                display: 'inline-block',
                textAlign: 'center',
                minWidth: '80px',
            };
        default:
            return {};
    }
}

function rowContent(_index, row) {
    return (
        <>
            {columns.map((column) => (
                <TableCell
                    key={column.dataKey}
                    align={column.numeric ? 'right' : 'left'}
                >
                    {column.dataKey === 'estado' ? (
                        <span style={getEstadoStyle(row.estado)}>
        {row.estado}
    </span>
                    ) : column.dataKey === 'nome' ? (
                        `${row.idcontrato} - ${row.nome}`
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

    useEffect(() => {
        const fetchContratos = async () => {
            try {
                const token = localStorage.getItem('token');
                const decoded = jwtDecode(token);
                const idUtilizador = parseInt(decoded.sub, 10);
                const tipo = parseInt(decoded.tipo, 10);

                const response = await axios.get('http://localhost:8080/Contratos/com-estado', {
                    headers: { Authorization: `Bearer ${token}` }
                });

                let contratos = response.data;

                // Filtrar contratos do próprio utilizador (caso tipo === 1)
                if (tipo === 1) {
                    contratos = contratos.filter(c => c.idutilizador === idUtilizador);
                }

                // Filtrar contratos com estado diferente de "Pendente"
                contratos = contratos.filter(c => c.idEstadoContrato !== 4);

                contratos.sort((a, b) => b.idcontrato - a.idcontrato);

                setRows(contratos);
            } catch (error) {
                console.error('Erro ao buscar contratos:', error);
            }
        };

        fetchContratos();
    }, []);


    return (
        <Paper style={{ height: 400, width: '90%', margin: '30px auto' }}>
            <TableVirtuoso
                data={rows}
                components={VirtuosoTableComponents}
                fixedHeaderContent={fixedHeaderContent}
                itemContent={rowContent}
            />
        </Paper>
    );
}
