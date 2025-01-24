import React, { useEffect, useState } from 'react';
import { Table } from 'react-bootstrap';
import { getAttributes } from '../../services/attributeService'; 
import { apiRequestHandler } from '../../utils/apiRequestHandler';

const Attributes: React.FC = () => {
    const [attributes, setAttributes] = useState<any[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        setIsLoading(true);
        apiRequestHandler(getAttributes, setAttributes)
            .finally(() => setIsLoading(false));
    }, []);

    return (
        <div>
        <h1>Attributes</h1>
        <Table striped>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th># Values</th>
                    <th>Values</th>
                </tr>
            </thead>
            <tbody>
                {attributes.map((attribute) => (
                    <tr key={attribute.id}>
                        <td>{attribute.id}</td>
                        <td>{attribute.name}</td>
                        <td>{attribute.values.length}</td>
                        <td>{attribute.values.map((e: any) => e.value).join(', ')}</td>
                    </tr>
                ))}
            </tbody>
        </Table>
        </div>
    );
};

export default Attributes;
