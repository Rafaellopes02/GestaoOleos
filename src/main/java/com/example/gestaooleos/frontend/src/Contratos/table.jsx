import React, { useEffect, useState } from 'react';
import axios from 'axios';

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
                color: '#ef6c00',
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
                color: '#d32f2f',
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
        axios.get('http://localhost:8080/Contratos/com-estado')
            .then((response) => {
                setRows(response.data);
            })
            .catch((error) => {
                console.error('Erro ao buscar contratos:', error);
            });
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
