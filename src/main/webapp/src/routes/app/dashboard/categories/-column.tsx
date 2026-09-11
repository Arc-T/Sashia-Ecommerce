import type { Category } from "@/types/category";
import type { ColumnDef } from "@tanstack/react-table";

export const categoryListColumn: ColumnDef<Category>[] = [
  {
    accessorKey: "id",
    header: "row",
    enableSorting: true,
    cell: ({ row }) => <span>{row.index + 1}</span>,
  },
  {
    accessorKey: "name",
    header: "name",
    enableSorting: true,
    cell: ({ row }) => <span className="font-medium">{row.original.name}</span>,
  },
  {
    accessorKey: "icon",
    header: "icon",
    enableSorting: false,
    cell: ({ row }) => <span className="font-medium">{row.original.icon}</span>,
  },
  {
    accessorKey: "description",
    header: "description",
    enableSorting: false,
    cell: ({ row }) => <span>{row.original.description}</span>,
  },
  {
    id: "actions",
    header: () => <span className="sr-only">عملیات</span>,
    enableSorting: false,
    enableHiding: false,
    size: 60,
  },
];
