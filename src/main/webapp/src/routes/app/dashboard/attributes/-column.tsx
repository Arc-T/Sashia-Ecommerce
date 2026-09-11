import type { ColumnDef } from "@tanstack/react-table";
import type { Attribute } from "@/types/attribute";
import { Badge } from "@/components/ui/badge";
import { ActionsColumn } from "@/components/custom/datatable/action-column";
import { AttributeService } from "@/services/attribute-service";

export const attributeListColumn: ColumnDef<Attribute>[] = [
  {
    accessorKey: "name",
    header: "name",
    enableSorting: true,
    cell: ({ row }) => <span className="font-medium">{row.original.name}</span>,
  },
  {
    accessorKey: "isFilterable",
    header: "is_filterable",
    enableSorting: false,
    cell: ({ row }) => {
      const value = row.original.isFilterable;
      return value ? (
        <Badge variant="outline" className="bg-green-100 text-green-800">
          بله
        </Badge>
      ) : (
        <Badge variant="outline" className="bg-red-100 text-red-800">
          خیر
        </Badge>
      );
    },
  },
  {
    accessorKey: "category",
    header: "category_name",
    enableSorting: false,
    cell: ({ row }) => {
      const category = row.original.category;
      return <span>{category?.name ?? "-"}</span>;
    },
  },
  {
    id: "action",
    header: "actions",
    enableSorting: false,
    enableHiding: false,
    cell: ({ row }) => {
      return (
        <ActionsColumn
          item={row.original}
          remove={AttributeService.delete}
          resource="attributes"
        />
      );
    },
  },
];
